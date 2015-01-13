import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyAlgorithms {
	
	/***
	 * 将现有流量复制一份
	 * 
	 * @param flow
	 * @return a copy
	 */
	public static Map<String,float[]> CopyFlow(Map<String,float[]> flow)
	{
		Map<String,float[]> ret = new HashMap<String,float[]>();
		
		for(Map.Entry<String, float[]> entry: flow.entrySet())
		{
			ret.put(entry.getKey(), entry.getValue().clone());
		}
		
		return ret;
	}
	
	/***
	 * 根据概率计算流量
	 * 
	 * @param x - 流入量
	 * @param proba - 概率alpha,beta,gamma
	 * @return
	 */
	public static int[] sampleData(int x, float[] proba)
	{
		int[] ret = new int[proba.length];
		
		/*
		for(int i = 0;i<x;i++)
		{
			double r = rand.nextDouble();
			double p = 0.0;
			for(int j=0;j<proba.length;j++)
			{
				p+=proba[j];
				if ( r<=p)
				{
					ret[j]++;
					break;
				}
			}
		}
		*/
		for(int i=0;i<proba.length;i++)
		{
			ret[i] = (int) Math.ceil(((float)x) * proba[i]);
		}
		
		return ret;
	}
	
	/***
	 * 根据当前四个方向的流量，和转弯概率计算出车辆流动情况
	 * 
	 * @param trafficStatus - 四个方向的流量
	 * @param probaTurn - 转弯概率alpha,beta,gamma
	 * @return 车辆流动情况
	 */
	public static CrossFlow CalcCrossFlow(int[] trafficStatus, float[] probaTurn)
	{
		CrossFlow flow = new CrossFlow();
		int[] t = sampleData(trafficStatus[0],probaTurn);
		
		flow.flowL2U = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowL2R = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowL2D = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		
		t = sampleData(trafficStatus[1],probaTurn);
		flow.flowU2R = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowU2D = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowU2L = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		
		t = sampleData(trafficStatus[2],probaTurn);
		flow.flowR2D = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowR2L = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowR2U = Math.min((int) Constants.MAX_THROUGH[2],t[2]);		
		
		t = sampleData(trafficStatus[3],probaTurn);
		flow.flowD2L = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowD2U = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowD2R = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		
		return flow;
	}
	
	/***
	 * 
	 * @param traffic
	 * @param time
	 * @return
	 */
	public static float SolveSingle(TrafficGraph traffic, int time)
	{

		
		//u
		Map<String,float[]> cost = traffic.getCurrentFlow();
		
		Map<String,Integer> setting = new HashMap<String,Integer>();
	
		
		traffic.setLight(setting, time);
		
		traffic.saveCurrentFlow();
		return 0.1f;
	}
	
	
	/***
	 * 
	 * @param traffic
	 * @param flow
	 * @param time
	 * @return
	 */
	public static float Solve(TrafficGraph traffic, Map<String,int[]> flow, int time)
	{
		traffic.setCurrentFlow(flow);
		return SolveSingle(traffic,time);
	}
}
