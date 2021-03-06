import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Algorithms 
{
	static Random rand = new Random();
	
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
	 * 陈俊楠：做了一些更改，把转弯的概率修正为考虑了没有该路口的情况
	 * @param trafficStatus - 四个方向的流量
	 * @param probaTurn - 转弯概率alpha,beta,gamma
	 * @return 车辆流动情况
	 */
	public static CrossFlow CalcCrossFlow(int[] trafficStatus,TrafficCrossroad cross)
	{
		CrossFlow flow = new CrossFlow();
		float [] probaTurn={0.1f,0.8f,0.1f};
		float [] leftprobTurn=probaTurn;
		
	
		if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
				leftprobTurn[0]=0;
				leftprobTurn[1]=0;
				leftprobTurn[2]=1;
			}else if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
				leftprobTurn[0]=0;
				leftprobTurn[1]=1;
				leftprobTurn[2]=0;
			}else{
				
				leftprobTurn[1]=leftprobTurn[1]+leftprobTurn[0]/2;
				leftprobTurn[2]=leftprobTurn[2]+leftprobTurn[0]/2;
				leftprobTurn[0]=0;
			}
		}else if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
				leftprobTurn[0]=1;
				leftprobTurn[1]=0;
				leftprobTurn[2]=0;
			}else{
				leftprobTurn[0]=leftprobTurn[0]+leftprobTurn[1]/2;
				leftprobTurn[2]=leftprobTurn[2]+leftprobTurn[1]/2;
				leftprobTurn[1]=0;
			}
		}else if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
			leftprobTurn[0]=leftprobTurn[0]+leftprobTurn[2]/2;
			leftprobTurn[1]=leftprobTurn[1]+leftprobTurn[2]/2;
			leftprobTurn[2]=0;
		}
		int[] t = sampleData(trafficStatus[0],leftprobTurn);
		
		flow.flowL2U = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowL2R = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowL2D = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		
		float [] upprobTurn=probaTurn;
		if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
				upprobTurn[0]=0;
				upprobTurn[1]=0;
				upprobTurn[2]=1;
			}else if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
				upprobTurn[0]=0;
				upprobTurn[1]=1;
				upprobTurn[2]=0;
			}else{
				
				upprobTurn[1]=upprobTurn[1]+upprobTurn[0]/2;
				upprobTurn[2]=upprobTurn[2]+upprobTurn[0]/2;
				upprobTurn[0]=0;
			}
		}else if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
				upprobTurn[0]=1;
				upprobTurn[1]=0;
				upprobTurn[2]=0;
			}else{
				upprobTurn[0]=upprobTurn[0]+upprobTurn[1]/2;
				upprobTurn[2]=upprobTurn[2]+upprobTurn[1]/2;
				upprobTurn[1]=0;
			}
		}else if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
			upprobTurn[0]=upprobTurn[0]+upprobTurn[2]/2;
			upprobTurn[1]=upprobTurn[1]+upprobTurn[2]/2;
			upprobTurn[2]=0;
		}
		t = sampleData(trafficStatus[1],upprobTurn);
		flow.flowU2R = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowU2D = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowU2L = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		
		float [] rightprobTurn=probaTurn;
		if(cross.neighbours[3].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
				rightprobTurn[0]=0;
				rightprobTurn[1]=0;
				rightprobTurn[2]=1;
			}else if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
				rightprobTurn[0]=0;
				rightprobTurn[1]=1;
				rightprobTurn[2]=0;
			}else{
				
				rightprobTurn[1]=rightprobTurn[1]+rightprobTurn[0]/2;
				rightprobTurn[2]=rightprobTurn[2]+rightprobTurn[0]/2;
				rightprobTurn[0]=0;
			}
		}else if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
				rightprobTurn[0]=1;
				rightprobTurn[1]=0;
				rightprobTurn[2]=0;
			}else{
				rightprobTurn[0]=rightprobTurn[0]+rightprobTurn[1]/2;
				rightprobTurn[2]=rightprobTurn[2]+rightprobTurn[1]/2;
				rightprobTurn[1]=0;
			}
		}else if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
			rightprobTurn[0]=rightprobTurn[0]+rightprobTurn[2]/2;
			rightprobTurn[1]=rightprobTurn[1]+rightprobTurn[2]/2;
			rightprobTurn[2]=0;
		}
		t = sampleData(trafficStatus[2],rightprobTurn);
		flow.flowR2D = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowR2L = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowR2U = Math.min((int) Constants.MAX_THROUGH[2],t[2]);		
		
		float [] downprobTurn=probaTurn;
		if(cross.neighbours[0].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
				downprobTurn[0]=0;
				downprobTurn[1]=0;
				downprobTurn[2]=1;
			}else if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
				downprobTurn[0]=0;
				downprobTurn[1]=1;
				downprobTurn[2]=0;
			}else{
				
				downprobTurn[1]=downprobTurn[1]+downprobTurn[0]/2;
				downprobTurn[2]=downprobTurn[2]+downprobTurn[0]/2;
				downprobTurn[0]=0;
			}
		}else if(cross.neighbours[1].compareTo(Constants.LIGHT_NONE)==0){
			if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
				downprobTurn[0]=1;
				downprobTurn[1]=0;
				downprobTurn[2]=0;
			}else{
				downprobTurn[0]=downprobTurn[0]+downprobTurn[1]/2;
				downprobTurn[2]=downprobTurn[2]+downprobTurn[1]/2;
				downprobTurn[1]=0;
			}
		}else if(cross.neighbours[2].compareTo(Constants.LIGHT_NONE)==0){
			downprobTurn[0]=downprobTurn[0]+downprobTurn[2]/2;
			downprobTurn[1]=downprobTurn[1]+downprobTurn[2]/2;
			downprobTurn[2]=0;
		}
		t = sampleData(trafficStatus[3],downprobTurn);
		flow.flowD2L = Math.min((int) Constants.MAX_THROUGH[0],t[0]);
		flow.flowD2U = Math.min((int) Constants.MAX_THROUGH[1],t[1]);
		flow.flowD2R = Math.min((int) Constants.MAX_THROUGH[2],t[2]);
		

		return flow;
	}
	
	/***
	 * 计算往前走的cost
	 * 
	 * @param ecost - 根据前馈反馈算法计算出的边的权重
	 * @param ef - 车辆流动情况
	 * @return - 第一种状态和第二种状态的cost
	 */
	public static float[] JudgeForwardCost(float[] ecost, CrossFlow ef)
	{
		float[][] forwardCost = new float[2][];
		forwardCost[0] = new float[4];
		forwardCost[1] = new float[4];
		forwardCost[0][0] = ef.flowR2L + ef.flowU2L;
		forwardCost[0][1] = ef.flowL2U + ef.flowR2U;
		forwardCost[0][2] = ef.flowL2R + ef.flowD2R;
		forwardCost[0][3] = ef.flowL2D + ef.flowR2D;
		
		forwardCost[1][0] = ef.flowU2L + ef.flowD2L;
		forwardCost[1][1] = ef.flowD2U + ef.flowR2U;
		forwardCost[1][2] = ef.flowU2R + ef.flowD2R;
		forwardCost[1][3] = ef.flowU2D + ef.flowL2D;
		
		float[] ret = new float[2];
		
		for(int i=0;i<4;i++)
		{
			ret[0] += forwardCost[0][i] * ecost[i];
			ret[1] += forwardCost[1][i] * ecost[i];
		}
		
		return ret;
	}
	
	/***
	 * 计算第一种状态和第二种状态的cost
	 * 
	 * @param flow - 当前4个方向的流量
	 * @param ecost - 根据前馈反馈算法计算出的边的权重
	 * @param ef - 车辆流动情况
	 * @return 第一种状态和第二种状态的cost
	 */
	public static float[] JudgeCost(int[] flow, float[] ecost, CrossFlow ef)
	{
		//int tcost = Utils.ArraySum(flow);
		float[] costs = new float[2];
		costs[0] = ef.flowL2R+ef.flowL2D+ef.flowL2U
				+ef.flowR2L+ef.flowR2U + ef.flowR2D
				+ef.flowU2L+ef.flowD2R;
		costs[1] = ef.flowD2U+ef.flowD2L+ef.flowD2R
				+ef.flowU2D+ef.flowU2L+ef.flowU2R
				+ef.flowL2D+ef.flowR2U;
		
		//costs[0] = Math.max(0,tcost-costs[0]);
		//costs[1] = Math.max(0,tcost-costs[1]);
		costs[0] = -costs[0];
		costs[1] = -costs[1];
		float[] fcost = JudgeForwardCost(ecost,ef);
		
		costs[0] += fcost[0] * Constants.LAMBDA_2;
		costs[1] += fcost[1] * Constants.LAMBDA_2;
		
		return costs;
	}
	
	/***
	 * flow中frmId到dstId的流量加上addf
	 * 
	 * @param flow - 当前流量
	 * @param frmId - 来源节点
	 * @param dstId - 目的节点
	 * @param traffic - 交通结构图
	 * @param addf - 流量增加量
	 */
	private static void AddMapFlow(Map<String,float[]> flow, String frmId, String dstId, 
			TrafficGraph traffic, float addf)
	{
		TrafficCrossroad cross = traffic.crosses.get(dstId);
		
		if (cross != null)
		{
			String[] nns = cross.neighbours;
			
			for(int i=0;i<nns.length;i++)
			{
				if (nns[i].compareTo(frmId)==0)
				{
					flow.get(dstId)[i] += addf;
				}
			}
		}
	}
	
	/***
	 * 根据概率计算三个方向的流量
	 * 
	 * @param flow - 流入量
	 * @param turnProba - 转弯概率
	 * @return 三个方向的流量
	 */
	public static float[] CalcTurnFlow(float flow, float[] turnProba)
	{
		float[] turn = Utils.ArrayScale(turnProba, flow);
		for(int i=0;i<turn.length;i++)
		{
			turn[i] = Math.min(Constants.MAX_THROUGH[i], turn[i]);
		}
		return turn;
	}
	
	/***
	 * 根据当前流量计算各个节点的流出量
	 * 
	 * @param traffic - 交通结构图
	 * @param cflow - 当前流量
	 * @param turnProba - 转弯概率
	 * @return 各个节点的流出量
	 */
	public static Map<String,float[]> CalcOutFlow(TrafficGraph traffic, 
			Map<String,float[]> cflow, float[] turnProba)
	{
		Map<String,float[]> ret = new HashMap<String,float[]>();
		for(String cid : traffic.crosses.keySet())
		{
			ret.put(cid, new float[4]);
		}
		
		for(Map.Entry<String, float[]> entry : cflow.entrySet())
		{
			String cid = entry.getKey();
			float[] f = entry.getValue();
			String[] nns = traffic.crosses.get(cid).neighbours;
			
			for(int i=0;i<f.length;i++)
			{
				String frm = cid;
				float[] tf = CalcTurnFlow(f[i],turnProba);
				
				for(int j=0;j<tf.length;j++)
				{
					int idst = (i+j) % 4;
					String dst = nns[idst];
					
					AddMapFlow(ret,frm,dst,traffic,tf[j]);
				}
				f[i] -= Utils.ArraySum(tf);
			}
		}
		
		return ret;
	}
	
	/***
	 * 让流量自由流动，计算下个时间段的各个节点的流量
	 * 
	 * @param traffic 交通图
	 * @param currentFlow 当前流量
	 * @return 下个时间段的流量
	 */
	public static Map<String,float[]> Forward(TrafficGraph traffic, 
			Map<String,float[]> currentFlow)
	{
		Map<String,float[]> ret = CopyFlow(currentFlow);
		Map<String,float[]> outf = CalcOutFlow(traffic,ret,Constants.TURN_PROBA);
		
		FlowAdd(ret,outf);
		return ret;
	}
	
	/***
	 * 将cost回馈
	 * 
	 * @param traffic - 交通结构图
	 * @param currentCost - 当前的cost
	 * @return 回馈的cost
	 */
	public static Map<String,float[]> Backward(TrafficGraph traffic,
			Map<String,float[]> currentCost)
	{
		return CalcOutFlow(traffic,currentCost, Constants.TURN_PROBA_REV);
	}
	
	private static void FlowAdd(Map<String,float[]> dst, Map<String,float[]>src)
	{
		FlowAdd(dst,src,1.0f);
	}
	
	/***
	 * 将src的流量加入dst
	 * 
	 * @param dst - 目标流量
	 * @param src - 源流量
	 * @param scale - 乘因子
	 */	
	private static void FlowAdd(Map<String,float[]> dst, Map<String,float[]>src, float scale)
	{//dst = dst+src
		for(Map.Entry<String,float[]> entry : src.entrySet())
		{
			String cid = entry.getKey();
			float[] flow = entry.getValue();
			
			if ( dst.containsKey(cid))
			{
				Utils.ArrayAdd(dst.get(cid), flow, scale);
			}
			else
			{
				dst.put(cid, flow.clone());
			}
		}
	}
	
	/***
	 * 将流量放大或缩小
	 * 
	 * @param dst - 目标
	 * @param scale - 乘因子
	 */
	private static void FlowScale(Map<String,float[]> dst, float scale)
	{
		for(Map.Entry<String,float[]> entry : dst.entrySet())
		{
			entry.setValue(Utils.ArrayScale(entry.getValue(), scale));
		}
	}
	
	/***
	 * 通过将流量前馈反馈算出每个节点四条边的权重，边的权重越大代表了将来可能会有大流量，
	 * 所以越不能往这个方向走
	 *  
	 * @param traffic 交通图结构
	 * @param currentTime 当前时间
	 * @param interval 前馈时间个数
	 * @return 每个节点对应4条边的权重
	 */
	public static Map<String,float[]> FlowPropagate(TrafficGraph traffic, int currentTime,
			int interval)
	{
		Map<String,float[]> currentFlow = traffic.getCurrentFlow();
		
		//forward
		List<Map<String,float[]> >fflows = new ArrayList<Map<String,float[]> >();
		for(int i=1;(i<interval)&&((currentTime+i)<Constants.MAX_TIME);i++)
		{
			//前馈
			Map<String,float[]> nextFlow = Forward(traffic,currentFlow);
			
			//加上每个路口突然出现的流量
			traffic.flowAdd(nextFlow, currentTime+i);
			fflows.add(nextFlow);
			currentFlow = nextFlow;
		}
		
		//backward
		Map<String,float[]> cost = new HashMap<String,float[]>();
		
		int revInd = fflows.size()-1;
		for(int i=1;(i<interval)&&((currentTime+i)<Constants.MAX_TIME);i++)
		{
			FlowAdd(cost, fflows.get(revInd));
			Map<String,float[]> tcost = Backward(traffic,cost);
			FlowAdd(cost,tcost);
			revInd--;
		}
		
		return cost;
	}
	
	/***
	 * 检查公平原则
	 * 
	 * @param cid - 节点id
	 * @param time - 当前时间
	 * @param set - 当前设定
	 * @param traffic - 交通结构图
	 * @return 是否违反公平原则
	 */
	public static boolean IsMaxInterval(String cid, int time, int set, TrafficGraph traffic)
	{
		int[] history = traffic.crosses.get(cid).lightSettingHistory;
		
		if (time <= Constants.MAX_LIGHT_INTERVAL)
		{
			return false;
		}
		
		for(int i=time-1;i>=time-Constants.MAX_LIGHT_INTERVAL;i--)
		{
			if ( history[i] != set)
			{
				return false;
			}
		}
		return true;
	}
	
	/***
	 * 根据cost确定红绿灯的设定
	 * 
	 * @param cost - 当前penalty
	 * @param estimateCost - 根据前馈和反馈计算出的边的权重
	 * @param setting - 各个节点的设定,output
	 * @param time - 当前时间
	 * @param traffic -交通结构图
	 * @return 总cost
	 */
	public static float Judge(Map<String,float[]> cost,
			Map<String,float[]> estimateCost, Map<String,Integer> setting,int time,
			TrafficGraph traffic)
	{
		float tcost = 0.0f;
		
		for(Map.Entry<String, float[]> entry : cost.entrySet())
		{
			String cid = entry.getKey();
			float[] flow = entry.getValue();
			
			int[] flowInt = Utils.ArrayFloat2Int(flow);
			CrossFlow cf = CalcCrossFlow(flowInt,traffic.crosses.get(cid));
			float[] costs = JudgeCost(flowInt,estimateCost.get(cid), cf);
			
			int csetting = -1;
			if ( costs[0] > costs[1])
			{
				csetting = 1;
			}
			else
			{
				csetting = 0;			
			}
			
			if ( IsMaxInterval(cid,time,csetting,traffic))
			{
				traffic.TimeoutTimes++;
				csetting = 1-csetting;
			}
			setting.put(cid, csetting);
			tcost += costs[csetting];
		}
		//SimpleLog.info("" + setting.get("tl32"));
		return tcost;
	}
	
	/***
	 * 
	 * @param traffic
	 * @param time
	 * @return
	 */
	public static float SolveSingle(TrafficGraph traffic, int time)
	{
		//估计交通图中每条边的权值
		Map<String,float[]> estimateCost = FlowPropagate(traffic,time,
				Constants.ESTIMATE_INTERVAL);
		
		//将权值缩小一定比例
		FlowScale(estimateCost,Constants.LAMBDA_1);
		
		//u
		Map<String,float[]> cost = traffic.getCurrentFlow();
		
		Map<String,Integer> setting = new HashMap<String,Integer>();
		
		//根据权值和流量计算红绿灯状态
		float tcost = Judge(cost, estimateCost, setting, time, traffic);
	
		traffic.setLight(setting, time);
		
		traffic.saveCurrentFlow();
		return tcost;
	}
	/***
	 * 
	 * @param traffic
	 * @param time
	 * @return
	 */
	public static float SolveSingle2(TrafficGraph traffic, int time)
	{

		Map<String,float[]> cost = traffic.getCurrentFlow();
		Map<String,Integer> setting = new HashMap<String,Integer>();
		for(Map.Entry<String, float[]> entry:cost.entrySet()){
			int thisset=0;
			float flow[]=entry.getValue();
			String crossid=entry.getKey();
			for(int i=0;i<4;i++){
				flow[i]=Math.min(20, flow[i]);
			}
			if((flow[0]+flow[2])>(flow[1]+flow[3])){
				thisset=0;
			}else{
				thisset=1;
			}
			if ( IsMaxInterval(entry.getKey(),time,thisset,traffic))
			{
				traffic.TimeoutTimes++;
				thisset = 1-thisset;
			}
			setting.put(entry.getKey(), thisset);
		}
		
		
		//根据权值和流量计算红绿灯状态
		float tcost = 0;
	
		traffic.setLight(setting, time);
		
		traffic.saveCurrentFlow();
		return tcost;
		
	}
	
	/***
	 * 
	 * @param traffic
	 * @param time
	 * @return
	 */
	public static int SolveSingle3Single(TrafficGraph traffic,TrafficCrossroad Cross,float []flow,Map<String,Integer> setting,int time)
	{
				//u
				int set=0;
				Map<String,float[]> cost = traffic.getCurrentFlow();
				String [] crossneighber=Cross.neighbours;
				int neiberAfterFlowState[]=new int[4];
				int neiberAfterFlowNeed[]=new int[4];
				int thisCanfullNeed[]=new int[4];
				int NeiberNewIn[]=new int[4];
				int neibersetting[]= new int[4];
				//对最大流量限制
				for(int i=0;i<4;i++){
					flow[i]=Math.min(20, flow[i]);
				}
				//获得邻居的当前红绿灯设置
				for(int i=0;i<4;i++){
					if(crossneighber[i].compareTo(Constants.LIGHT_NONE)!=0){
						if(setting.containsKey(crossneighber[i])){
							neibersetting[i]=setting.get(crossneighber[i]);
						}else{
							neibersetting[i] = 10;//没有设置设置成10
						}
					}else{
						neibersetting[i] = 9;//空的状态为9
					}
				}
				//获得邻居节点流动后的状态
				for(int i=0;i<4;i++){
					float [] neiberFlow = new float[4] ;
					if(cost.containsKey(crossneighber[i])){
						neiberFlow = cost.get(crossneighber[i]);
					}else{
						for(int j=0;j<4;j++){
							neiberFlow[j]=0;
						}
					}//邻居的Flow状态
					if(neibersetting[i]<9){
						
						int fromID=traffic.findNeighbourIndex(crossneighber[i], Cross.id);
						if(neibersetting[i]==0){
							if(fromID==0||fromID==2){
								neiberAfterFlowState[i] = (int) (neiberFlow[fromID] - Math.min(20, neiberFlow[fromID]));
							}else{
								neiberAfterFlowState[i] = (int) neiberFlow[fromID];
							}
						}else if(neibersetting[i]==1){
							if(fromID==0||fromID==2){
								neiberAfterFlowState[i] = (int) neiberFlow[fromID];
							}else{
								neiberAfterFlowState[i] = (int) (neiberFlow[fromID] - Math.min(20, neiberFlow[fromID]));
							}
						}else{
							int rightID=traffic.crosses.get(crossneighber[i]).findTriRight();
							if(rightID ==fromID){
								neiberAfterFlowState[i] = (int) neiberFlow[fromID];
							}else if((fromID-rightID)%4==2){
								neiberAfterFlowState[i] = (int) (neiberFlow[fromID] - Math.min(20, neiberFlow[fromID]));
							}else{
								neiberAfterFlowState[i] = (int) (neiberFlow[fromID] - Math.min(4, neiberFlow[fromID]));
							}
						}
					}else{
						if(neibersetting[i]==9){
							neiberAfterFlowState[i] = 0;
						}else{//设置成10，不为空，但是没有设置
							int fromID=traffic.findNeighbourIndex(crossneighber[i], Cross.id);
							neiberAfterFlowState[i]=(int) neiberFlow[fromID];
						}
					}
				}
				//获得预估加入的量
				for(int i=0;i<4;i++){
					if(crossneighber[i].compareTo(Constants.LIGHT_NONE)==0){
						NeiberNewIn[i]=0;
					}else{
						if(traffic.crosses.containsKey(crossneighber[i])){
							int fromID=traffic.findNeighbourIndex(crossneighber[i], Cross.id);
							NeiberNewIn[i] = traffic.crosses.get(crossneighber[i]).flowAdd[time+1][fromID]/2;//有可能报错
						}else {
							NeiberNewIn[i]=0;
						}
						
					}
				}
				//获得需求量
				for(int i=0;i<4;i++){
					if(traffic.crosses.containsKey(crossneighber[i])){
						int fromID=traffic.findNeighbourIndex(crossneighber[i], Cross.id);
						if(IsTriangle(traffic.crosses.get(crossneighber[i]))){
							int right = traffic.crosses.get(crossneighber[i]).findTriRight();
							if((fromID-right)%4==3){
								neiberAfterFlowNeed[i] = Math.max(0,4- (neiberAfterFlowState[i]+NeiberNewIn[i]));
							}else{
								neiberAfterFlowNeed[i] = Math.max(0,20- (neiberAfterFlowState[i]+NeiberNewIn[i]));
							}
						}else{
							neiberAfterFlowNeed[i] = Math.max(0,20- (neiberAfterFlowState[i]+NeiberNewIn[i]));
						}
					}else if(crossneighber[i].compareTo(Constants.LIGHT_NONE)==0){
						neiberAfterFlowNeed[i]=0;
					}else{
						neiberAfterFlowNeed[i]=999;
					}
				}
				
				//更新可满足量
				for(int i=0;i<4;i++){
					thisCanfullNeed[i]=(int) Math.min(neiberAfterFlowNeed[i], flow[i]);
				}
				//再次更新需求量
				for(int i=0;i<4;i++){
					if(traffic.crosses.containsKey(crossneighber[i])&&traffic.crosses.get(crossneighber[i]).findHighestLevel()==10){
						thisCanfullNeed[i] = 0;
					}
					if(Cross.neighboursLevel[i]==10){
						thisCanfullNeed[i] = 0;
					}
				}
				//决定set
				boolean onMainRoad=false;
				for(int i=0;i<4;i++){
					if(Cross.neighboursLevel[i]<10){
						 onMainRoad=true;
					}
				}
				if(IsTriangle(Cross)){
					//更新计算可流动的最大量
					int RightID=Cross.findTriRight();
					for(int i=0;i<4;i++){
						if((RightID-i)%4==1){
							flow[i]=Math.min(4, flow[i]);
						}
					}
					if(onMainRoad){
						if((thisCanfullNeed[0]+thisCanfullNeed[2])>(thisCanfullNeed[1]+thisCanfullNeed[3])){
							set=0;
						}else{
							set=1;
						}
					}else{
						if((flow[0]+flow[2])>(flow[1]+flow[3])){
							set=0;
						}else{
							set=1;
						}
					}
				}else{
					
					if(onMainRoad){
						if((thisCanfullNeed[0]+thisCanfullNeed[2])>(thisCanfullNeed[1]+thisCanfullNeed[3])){
							set=0;
						}else{
							set=1;
						}
					}else{
						if((flow[0]+flow[2])>(flow[1]+flow[3])){
							set=0;
						}else{
							set=1;
						}
					}
				}
				return set;
	}
	public static int SolveSingle4Single(TrafficGraph traffic,TrafficCrossroad Cross,float []flow,Map<String,Integer> setting,int time)
	{
				//u
				int set=0;
				Map<String,float[]> cost = traffic.getCurrentFlow();
				String [] crossneighber=Cross.neighbours;
				int neiberFlowState[]=new int[4];
				float delta =1f;
				float Score[]=new float[4];
				//对最大流量限制
				for(int i=0;i<4;i++){
					flow[i]=Math.min(20, flow[i]);
					Score[i]+=flow[i];
				}
				
				for(int i=0;i<4;i++){
					if(crossneighber[i].compareTo(Constants.LIGHT_NONE)==0){
						neiberFlowState[i]=18;
					}else{
						if(traffic.crosses.containsKey(crossneighber[i])){
							float []neiberFLow = cost.get(crossneighber[i]);
							int fromID=traffic.findNeighbourIndex(crossneighber[i], Cross.id);
							neiberFlowState[i]=(int) neiberFLow[fromID];
						}else{
							neiberFlowState[i]=0;
						}
					}
				}
				for(int i=0;i<4;i++){
					int othersid = (i+2)%4;
					Score[i]+=Math.max(0,(20-neiberFlowState[othersid])*delta);
				}
				if((Score[0]+Score[2])>(Score[1]+Score[3])){
					set=0;
				}else{
					set=1;
				}
				if ( IsMaxInterval(Cross.id,time,set,traffic))
				{
					traffic.TimeoutTimes++;
					set = 1-set;
				}
				return set;
	}
	public static float SolveSingle3(TrafficGraph traffic, int time){
		float ret=0f;
		Map<String,Integer> setting = new HashMap<String,Integer>();
		Map<String,float[]> cost = traffic.getCurrentFlow();
		for(int i=1;i<11;i++){
			for(TrafficCrossroad cross:traffic.crosses.values()){
				if(cross.findHighestLevel()==i){
					String ID=cross.id;
					int set = 0;
					float [] flow=cost.get(cross.id);
					set=SolveSingle3Single(traffic,cross,flow,setting,time);
					setting.put(cross.id, set);
				}
			}
		}
		traffic.setLight(setting, time);
		
		traffic.saveCurrentFlow();
		return ret;
	}
	public static float SolveSingle4(TrafficGraph traffic, int time){
		float ret=0f;
		Map<String,Integer> setting = new HashMap<String,Integer>();
		Map<String,float[]> cost = traffic.getCurrentFlow();
		for(TrafficCrossroad cross:traffic.crosses.values()){
					String ID=cross.id;
					int set = 0;
					float [] flow=cost.get(cross.id);
					set=SolveSingle4Single(traffic,cross,flow,setting,time);
					setting.put(ID, set);
		}
		traffic.setLight(setting, time);
		
		traffic.saveCurrentFlow();
		return ret;
	}
	public static int [] Set3CanGo(TrafficCrossroad Cross,int []flow){
		int ret[]=new int[4];
		int right=Cross.findTriRight();
		ret[right]=0;
		int NoneOther=(right+3)%4;
		ret[NoneOther]=Math.min(flow[NoneOther], 4);
		return ret;
	}
	
	public static boolean IsTriangle(TrafficCrossroad Cross){
		boolean IsTri=false;
		for(String i:Cross.neighbours){
			if(i.compareTo(Constants.LIGHT_NONE)==0){
				IsTri=true;
			}
		}
		return IsTri;
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
		for(int [] m:flow.values()){
			traffic.Total+=Utils.ArraySum(m);
		}
		

		return SolveSingle4(traffic,time);

		//return SolveSingle2(traffic,time);

	}
}