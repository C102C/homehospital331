package com.krl.data;

public class QRSComplexDetect
{
	// 微分
	class Difference
	{
		public int		m_nSize;			// 缓冲区大小
		public int		m_nCurPos;			// 当前位置
		public int []	m_pBuffer;			// 缓冲区
		public int		m_nLastElement;	// 上一个值
		public int		m_nLastDiffValue;	// 上一个差分值

		public Difference(int nSize)
		{
			m_nSize = nSize;
			m_nCurPos = 0;
			m_nLastElement = 0;
			m_pBuffer = new int[m_nSize];
		}

		int Size()
		{
			return m_nSize;
		}

		int Push(int nVal)
		{
			m_nLastDiffValue = nVal - m_nLastElement;
			m_pBuffer[m_nCurPos] = m_nLastDiffValue;
			m_nLastElement = nVal;
			m_nCurPos++;
			m_nCurPos %= m_nSize;
			return m_nLastDiffValue;
		}
	}

	// 滑动平均值
	class SlidingAverage
	{
		public SlidingAverage(int nWindow)
		{
			m_nSize = nWindow;
			m_dCount = m_dAvg = 0;
			m_nCurPos = 0;
			m_pBuffer = new int[m_nSize];
			m_bInit = true;
		}

		int Size()
		{
			return m_nSize;
		}

		double GetAvg()
		{
			return m_dAvg;
		}

		int GetMin(int nRange)
		{
			int nMin = 0;
			if (nRange > m_nSize)
			{
				nRange = m_nSize;
			}
			int i = (m_nSize + m_nCurPos - nRange) % m_nSize;
			do
			{
				if (m_pBuffer[i] < nMin)
				{
					nMin = m_pBuffer[i];
				}
				i++;
				i %= m_nSize;
			}
			while (i != m_nCurPos);
			return nMin;
		}

		int GetMax(int nRange)
		{
			int nMax = 0;
			if (nRange > m_nSize)
			{
				nRange = m_nSize;
			}
			int i = (m_nSize + m_nCurPos - nRange) % m_nSize;
			do
			{
				if (m_pBuffer[i] > nMax)
				{
					nMax = m_pBuffer[i];
				}
				i++;
				i %= m_nSize;
			}
			while (i != m_nCurPos);
			return nMax;
		}

		Boolean IsInit()
		{
			return m_bInit;
		}

		// 插入一个新值，返回平均值
		double Push(int nVal)
		{
			m_dCount += nVal;
			if (m_bInit)
			{
				m_pBuffer[m_nCurPos] = nVal;
				if (m_nCurPos == m_nSize - 1)
				{
					// 初始化完成
					m_bInit = false;
					m_dAvg = m_dCount / (double) m_nSize;
				}
			}
			else
			{
				m_dCount -= m_pBuffer[m_nCurPos];
				m_dAvg = m_dCount / (double) m_nSize;
				m_pBuffer[m_nCurPos] = nVal;
			}
			m_nCurPos++;
			m_nCurPos %= m_nSize;
			return m_dAvg;
		}

		private int			m_nSize;	// 窗口大小
		private int []		m_pBuffer;	// 窗口缓冲区
		private Boolean		m_bInit;	// 初始化完成标志
		private double		m_dCount;	// 窗口内元素的总和
		private int			m_nCurPos;	// 当前元素的位置
		protected double	m_dAvg;	// 最新平均值
	};

	// 阀值
	class Threshold extends SlidingAverage
	{
		int	m_nExtremumWindowSize;	// 采样极值的窗口大小
		int	m_nExtremum;			// 最新极值
		int	m_nExtremumIndex;		// 极值更新的计数器

		public Threshold(int nWindow, int nExtWindow)
		{
			super(nWindow);
			m_nExtremumWindowSize = nExtWindow;
			m_nExtremum = 0;
			m_nExtremumIndex = 0;
		}

		double Update(int nVal, Boolean bMax)
		{
			// 取窗口内的极值
			if (bMax)
			{
				if (nVal > m_nExtremum)
				{
					m_nExtremum = nVal;
				}
			}
			else
			{
				if (nVal < m_nExtremum)
				{
					m_nExtremum = nVal;
				}
			}
			m_nExtremumIndex++;
			m_nExtremumIndex %= m_nExtremumWindowSize;
			// ////////////////////////////////////////////////////////////////////////
			// 保存极值并从中取平均数
			if (m_nExtremumIndex == 0)
			{
				Push(m_nExtremum);
				m_nExtremum = 0;
			}
			return m_dAvg;
		}
	};

	public QRSComplexDetect(int nFrequency)
	{
		m_nFrequency = nFrequency;
		m_nRR = 1;
		m_nRRMin = 48;
		m_dRRRatioMin = 0.2;
		m_dRRRatioMax = 20;
		m_nRRDiffMax = m_nFrequency * 10;
		m_nMinPositive = 40;
		m_nMaxNegative = -40;
		m_nState = 0;
		m_dAvgRR = 0;
		m_dThresholdRatio = 0.7;
		m_nLastPos = 0;
		m_nCurPos = 0;
		m_dErrorThreshold = 2.5;
		m_bLastIsErrorSignal = false;
		m_dPVCThreshold = 0.3;
	}

	void Init(int nDiffSize, int nThreWndSize, int nExetWndSize, int nRRSize)
	{
		m_pFirstDiff = new Difference(nDiffSize);
		m_pSecondDiff = new Difference(nDiffSize);
		m_pThresholdNegative = new Threshold(nThreWndSize, nExetWndSize);
		m_pThresholdPositive = new Threshold(nThreWndSize, nExetWndSize);
		m_pRR = new SlidingAverage(nRRSize);
		m_pSrcPositiveBuffer = new SlidingAverage(m_nFrequency / 5);
		m_pSrcNegativeBuffer = new SlidingAverage(m_nFrequency / 5);
	}

	// 传入一个采样点，获得更新后的心率
	Boolean GetHR(int [] nHR, int nVal)
	{
		Boolean ret = false;
		// 取得最新微分值
		int nFirstDiffValue = m_pFirstDiff.Push(nVal);
		int nSecondDiffValue = m_pSecondDiff.Push(nFirstDiffValue);
		int nDiffValue = nFirstDiffValue;
		nHR[1] = nDiffValue;
		// RR间期
		int nRRPositive = 0, nRRNegative = 0;
		// 是否是有效数据
		Boolean bValid = false;
		// 过滤掉基线信号和剧烈波动的干扰信号
		int nSignal = 0;
		if (nDiffValue > m_nMinPositive)
		{
			nSignal = nDiffValue - m_nMinPositive;
			if (m_pThresholdPositive.IsInit())
			{
				m_pThresholdPositive.Update(nSignal, true);
				m_pSrcPositiveBuffer.Push(nVal);
				bValid = true;
			}
			else if (nSignal < m_pThresholdPositive.GetAvg() * m_dErrorThreshold)
			{
				m_pThresholdPositive.Update(nSignal, true);
				m_pSrcPositiveBuffer.Push(nVal);
				bValid = true;
			}
			else
			{
				m_bLastIsErrorSignal = true;
			}
		}
		else if (nDiffValue < m_nMaxNegative)
		{
			nSignal = nDiffValue - m_nMaxNegative;
			if (m_pThresholdNegative.IsInit())
			{
				m_pThresholdNegative.Update(nSignal, false);
				m_pSrcNegativeBuffer.Push(nVal);
				bValid = true;
			}
			else if (nSignal > m_pThresholdNegative.GetAvg() * m_dErrorThreshold)
			{
				m_pThresholdNegative.Update(nSignal, false);
				m_pSrcNegativeBuffer.Push(nVal);
				bValid = true;
			}
			else
			{
				m_bLastIsErrorSignal = true;
			}
		}
		int nLastState = m_nState;
		if (bValid)
		{
			nRRPositive = (int) m_pThresholdPositive.GetAvg();
			nRRNegative = (int) m_pThresholdNegative.GetAvg();
			int nThrePositive = (int) (nRRPositive * m_dThresholdRatio);
			int nThreNegative = (int) (nRRNegative * m_dThresholdRatio);
			int nPVCPositive = (int) (nRRPositive * m_dPVCThreshold);
			int nPVCNegative = (int) (nRRNegative * m_dPVCThreshold);
			// 如果波形向下
			if (Math.abs(nRRNegative) > nRRPositive)
			{
				if (m_nState <= 0)
				{
					if (nSignal < nThreNegative)
					{
						m_nState = 1;
					}
					else if (nSignal < nPVCNegative)
					{// 有可能是宽大畸形
						if (m_pSrcPositiveBuffer.GetMax(5) / m_pSrcPositiveBuffer.GetAvg() > 1.0 && nSignal / m_pThresholdNegative.GetAvg() < 1.0)
						{
							m_nState = 1;
						}
					}
				}
			}
			else
			{
				if (m_nState <= 0)
				{
					if (nSignal > nThrePositive)
					{
						m_nState = 1;
					}
					else if (nSignal > nPVCPositive)
					{// 有可能是宽大畸形
						if (m_pSrcNegativeBuffer.GetMin(5) / m_pSrcNegativeBuffer.GetAvg() > 1.0 && nSignal / m_pThresholdPositive.GetAvg() < 1.0)
						{
							m_nState = 1;
						}
					}
				}
			}
		}
		else
		{
			m_nState = 0;
		}
		int nRR = m_nCurPos - m_nLastPos;
		if (nLastState == 0 && m_nState == 1)
		{
			if (m_nRR <= 0)
			{
				m_nRR = nRR;
			}
			if (nRR > 0)
			{
				double dRRRatio = (double) nRR / (double) m_nRR;
				if (nRR >= m_nRRMin && dRRRatio > m_dRRRatioMin && dRRRatio < m_dRRRatioMax && Math.abs(nRR - m_nRR) < m_nRRDiffMax)
				{
					m_nRR = nRR;
					m_nLastPos = m_nCurPos;
					if (!m_bLastIsErrorSignal || Math.abs(nRR - m_nRR) < 20)
					{
						m_dAvgRR = m_pRR.Push(m_nRR);
						ret = true;
					}
					m_bLastIsErrorSignal = false;
				}
				else
				{
					m_bLastIsErrorSignal = true;
				}
				if (dRRRatio > m_dRRRatioMax)
				{
					m_nLastPos = m_nCurPos;
					m_nRR = 0;
					m_bLastIsErrorSignal = true;
				}
			}
		}
		else if (nRR > m_nFrequency * 12)
		{
			nHR[0] = 0;
			m_nCurPos++;
			return true;
		}
		m_nCurPos++;
		if (!m_pRR.IsInit())
		{
			nHR[0] = (int) (60.0 / ((double) m_dAvgRR / (double) m_nFrequency) + 0.5);
			return ret;
		}
		nHR[0] = 0;
		return ret;
	}

	private double			m_dThresholdRatio;		// 计算阀值采用的百分比
	private int				m_nFrequency;			// 采集频率
	private Difference		m_pFirstDiff;			// 一阶微分
	private Difference		m_pSecondDiff;			// 二阶微分
	private Threshold		m_pThresholdPositive;	// 正向阀值
	private Threshold		m_pThresholdNegative;	// 负向阀值
	private SlidingAverage	m_pRR;					// 平均RR间期
	private int				m_nRRMin;				// RR下限
	private double			m_dRRRatioMin;			// RR比下限
	private double			m_dRRRatioMax;			// RR比上限
	private double			m_nRRDiffMax;			// RR差上限
	private int				m_nMinPositive;		// 正向信号下限
	private int				m_nMaxNegative;		// 负向信号上限
	private int				m_nState;				// 状态
	private int				m_nRR;					// 当前RR间期
	private double			m_dAvgRR;				// 当前平均RR间期
	private int				m_nLastPos;			// 上一个正常心搏的位置
	private int				m_nCurPos;				// 当前位置
	private double			m_dErrorThreshold;		// 干扰数据排除阀值
	private Boolean			m_bLastIsErrorSignal;	// 上次是否是干扰数据
	private SlidingAverage	m_pSrcPositiveBuffer;	// 原始正向数据缓冲
	private SlidingAverage	m_pSrcNegativeBuffer;	// 原始负向数据缓冲
	private double			m_dPVCThreshold;		// 宽大畸形的幅度阀值
};
