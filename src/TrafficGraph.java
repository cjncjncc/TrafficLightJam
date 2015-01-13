import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * 
 * 交通结构图
 *
 */
public class TrafficGraph 
{
	public Map<String, TrafficCrossroad> crosses; //所有节点
	public int penalty;
	public int TimeoutTimes;
	public int flowAway;
	TrafficGraph()
	{
		this.crosses = new HashMap<String,TrafficCrossroad>();
		penalty=0;
		TimeoutTimes=0;
		flowAway=0;
	}
	
	
	/***
	 * 从reader中读入交通结构
	 * 
	 * @param reader - 输入
	 * @throws IOException
	 */
	public void load(BufferedReader reader) throws IOException
	{
		Map<String, List<String[]> > preMap = new HashMap<String, List<String[]> >();

		String line  = "";
		
		while(line != null)
		{
			line = reader.readLine();
			if (line == null)
			{
				break;
			}
			line = line.trim();
			String[] parts = line.split(",");
			String[] otherParts = Arrays.copyOfRange(parts,1,parts.length);
			
			if (parts.length != 5)
			{
				System.out.println(line);
				reader.close();
				throw new RuntimeException("logic error" + "part's length:" + parts.length 
						+ line);
			}
			if (preMap.containsKey(parts[0]))
			{
				preMap.get(parts[0]).add(otherParts);
			}
			else
			{
				List<String[]> lists = new ArrayList<String[]>();
				lists.add(otherParts);
				preMap.put(parts[0], lists);
			}
		}
		
		reader.close();
		
		for(Map.Entry<String, List<String[]> > entry : preMap.entrySet())
		{
			String cid = entry.getKey().trim();
			List<String[]> lists = entry.getValue();
			String left = lists.get(0)[0].trim();
			String up = Constants.LIGHT_NONE;
			String right = Constants.LIGHT_NONE;
			String down = Constants.LIGHT_NONE;
			
			for(int i=1;i<lists.size();i++)
			{
				String[] rec = lists.get(i);
				String from = rec[0].trim();
				String leftTarget = rec[1].trim();
				String rightTarget = rec[2].trim();
				String straightTarget = rec[3].trim();
				
				if ( leftTarget.compareTo(left)==0)
				{
					down = from;
				}
				if ( rightTarget.compareTo(left)==0)
				{
					up = from;
				}
				if ( straightTarget.compareTo(left)==0)
				{
					right = from;
				}
			}
			
			TrafficCrossroad cross = new TrafficCrossroad(cid);
			cross.setNeightbours(left, up, right, down);
			crosses.put(cid, cross);
		}   		
		
	}
	public void load(String filename) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		this.load(reader);		
	}
	
	
	/***
	 * 读入各个路口突然出现的流量
	 * 
	 * @param reader - 输入
	 * @throws IOException
	 */
	public void loadFlowAdd(BufferedReader reader) throws IOException
	{	
		String line = reader.readLine();
		
		while(line != null)
		{
			line = line.trim();
			String parts[] = line.split(",");
			String frmId = parts[1];
			String dstId = parts[0];
			TrafficCrossroad vertex = this.crosses.get(dstId);
			if (vertex != null)
			{
				String[] flows = Arrays.copyOfRange(parts,2,parts.length);
				for(int i=0;i<4;i++)
				{
					if(vertex.neighbours[i].compareTo(frmId)==0)
					{
						for(int j=0;j<flows.length;j++)
						{
							vertex.flowAdd[j][i] = Integer.parseInt(flows[j]);
						}
					}
				}
			}
			line = reader.readLine();
		}		
	}
	public void loadFlowAdd(String filename) throws IOException
	{	
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		this.loadFlowAdd(reader);
		reader.close();
	}
	
	
	/***
	 * 取得当前流量
	 * 
	 * @return
	 */
	public Map<String,float[]> getCurrentFlow()
	{
		Map<String,float[]> ret = new HashMap<String,float[]>();
		for(TrafficCrossroad cross: this.crosses.values())
		{
			float[] f = new float[4];
			for(int i=0;i<f.length;i++)
			{
				f[i] = (float) cross.currentFlow[i];
			}
			ret.put(cross.id, f);
		}
		return ret;
	}
	
	/***
	 * 取得第time时刻的流量
	 * 
	 * @param time
	 * @return
	 */
	public Map<String,int[]> getFlow(int time)
	{
		Map<String,int[]> ret = new HashMap<String,int[]>();
		for(TrafficCrossroad cross: this.crosses.values())
		{
			int[] f = new int[4];
			for(int i=0;i<f.length;i++)
			{
				f[i] = (int) cross.flowHistory.get(time)[i];
			}
			ret.put(cross.id, f);
		}
		return ret;		
	}
	
	/***
	 * 取得节点cid的第time时刻的流量
	 * 
	 * @param cid
	 * @param time
	 * @return
	 */
	public int[] getFlowAdd(String cid, int time)
	{
		return this.crosses.get(cid).flowAdd[time];
	}
	/***
	 * 取得下一个时间节点预估突然出现的流量Flow
	 * @param time
	 * @return flow
	 */
	public Map<String,int []> getFlowAddAll(int time){
		Map<String,int []> ret =new HashMap<String,int[]>();
		for(Map.Entry<String, TrafficCrossroad>entry: this.crosses.entrySet()){
			int [] input =this.getFlowAdd(entry.getKey(), time);
			ret.put(entry.getKey(), input);
		}
		return ret;
	}
	/***
	 * 将第time时刻的流量加上flow
	 * 
	 * @param flow
	 * @param time
	 */
	public void flowAdd(Map<String,float[]>flow, int time)
	{
		for(Map.Entry<String, float[]>entry: flow.entrySet())
		{
			String cid = entry.getKey();
			float[] cflow = entry.getValue();
			int[] flowAdd = this.getFlowAdd(cid, time);
			
			Utils.ArrayAdd(cflow, flowAdd);
		}
	}
	
	public void setLight(String cid, int setting, int time)
	{
		this.crosses.get(cid).setLight(setting, time);
	}
	
	public void setLight(Map<String,Integer> setting, int time)
	{
		for(Map.Entry<String, Integer> entry : setting.entrySet())
		{
			this.setLight(entry.getKey(), entry.getValue(),time);
		}
	}
	
	public void saveCurrentFlow()
	{
		for(Map.Entry<String, TrafficCrossroad> entry: crosses.entrySet())
		{
			TrafficCrossroad cross = entry.getValue();
			cross.flowHistory.add(cross.currentFlow.clone());
		}
	}
	
	public void setCurrentFlow(Map<String,int[]> flow)
	{
		for(Map.Entry<String, TrafficCrossroad> entry: this.crosses.entrySet())
		{
			String cid = entry.getKey();
			TrafficCrossroad cross = entry.getValue();
			cross.currentFlow = flow.get(cid).clone();
		}
	}
	
	/***
	 * 找出frmId在dstId的哪个方向
	 * 
	 * @param dstId
	 * @param frmId
	 * @return
	 */
	public int findNeighbourIndex(String dstId, String frmId)
	{
		TrafficCrossroad cr = this.crosses.get(dstId);
		for(int i=0;i<4;i++)
		{
			if ( cr.neighbours[i].compareTo(frmId)==0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/***
	 * 根据当前时间time计算下一次的流量
	 * 陈俊楠：目前没有调用
	 * @param time
	 * @return
	 */
	public Map<String,int[]> computeNextFlow(int time)
	{
		Map<String,int[]> flow = new HashMap<String,int []>();
		flow = this.getFlow(time);
		Map<String,int[]> flowAdd=this.getFlowAddAll(time+1);
		for(TrafficCrossroad cross : this.crosses.values())
		{
			String cid = cross.id;
			int setting = cross.lightSettingHistory[time];
			
			CrossFlow cf = Algorithms.CalcCrossFlow(cross.flowHistory.get(time),cross);
			if ( setting==0)
			{
				cf.flowD2L = 0;
				cf.flowD2U = 0;
				cf.flowU2D = 0;
				cf.flowU2R = 0;
			}
			else if (setting==1)
			{
				cf.flowL2U = 0;
				cf.flowL2R = 0;
				cf.flowR2L = 0;
				cf.flowR2D = 0;
			}
			
			
			int[] f = flow.get(cid);
			f[0] -= cf.flowL2D+cf.flowL2R+cf.flowL2U;
			f[1] -= cf.flowU2D+cf.flowU2L+cf.flowU2R;
			f[2] -= cf.flowR2D+cf.flowR2L+cf.flowR2U;
			f[3] -= cf.flowD2L+cf.flowD2R+cf.flowD2U;
			this.flowAway+=cf.flowD2L+cf.flowD2R+cf.flowD2U+cf.flowR2D+cf.flowR2L+cf.flowR2U+cf.flowU2D+cf.flowU2L+cf.flowU2R+cf.flowL2D+cf.flowL2R+cf.flowL2U;
			for(int i=0;i<4;i++){
				if(f[i]<0){
					f[i]=0;
				}
			}
			this.penalty+=Utils.ArraySum(f);
	
			
			if ( cross.neighbours[0].compareTo(Constants.LIGHT_NONE)!=0)
			{
				if(this.crosses.get(cross.neighbours[0])!=null){
					TrafficCrossroad LeftCross =this.crosses.get(cross.neighbours[0]);
					CrossFlow lcf = Algorithms.CalcCrossFlow(LeftCross.flowHistory.get(time),LeftCross);
					
					int setting2 = LeftCross.lightSettingHistory[time];
					if ( setting2==0)
					{
						lcf.flowD2L = 0;
						lcf.flowD2U = 0;
						lcf.flowU2D = 0;
						lcf.flowU2R = 0;
					}
					else if (setting2==1)
					{
						lcf.flowL2U = 0;
						lcf.flowL2R = 0;
						lcf.flowR2L = 0;
						lcf.flowR2D = 0;
					}
				
					f[0] += lcf.flowD2R+lcf.flowL2R+lcf.flowU2R + flowAdd.get(cross.id)[0]/2;
				}else{
					f[0] += flowAdd.get(cross.id)[0];
				}
			}
			
			if ( cross.neighbours[1].compareTo(Constants.LIGHT_NONE)!=0)
			{
				if(this.crosses.get(cross.neighbours[1])!=null){
					TrafficCrossroad upCross =this.crosses.get(cross.neighbours[1]);
					CrossFlow upcf = Algorithms.CalcCrossFlow(upCross.flowHistory.get(time),upCross);
					int setting2 = upCross.lightSettingHistory[time];
					if ( setting2==0)
					{
						upcf.flowD2L = 0;
						upcf.flowD2U = 0;
						upcf.flowU2D = 0;
						upcf.flowU2R = 0;
					}
					else if (setting2==1)
					{
						upcf.flowL2U = 0;
						upcf.flowL2R = 0;
						upcf.flowR2L = 0;
						upcf.flowR2D = 0;
					}
				
					f[1] += upcf.flowL2D+upcf.flowU2D+upcf.flowR2D + flowAdd.get(cross.id)[1]/2;
				}else{
					f[1] += flowAdd.get(cross.id)[1];
				}
			}
			
			if ( cross.neighbours[2].compareTo(Constants.LIGHT_NONE)!=0)
			{
				if(this.crosses.get(cross.neighbours[2])!=null){
					TrafficCrossroad RightCross =this.crosses.get(cross.neighbours[2]);
					CrossFlow rightcf = Algorithms.CalcCrossFlow(RightCross.flowHistory.get(time),RightCross);
					int setting2 = RightCross.lightSettingHistory[time];
					if ( setting2==0)
					{
						rightcf.flowD2L = 0;
						rightcf.flowD2U = 0;
						rightcf.flowU2D = 0;
						rightcf.flowU2R = 0;
					}
					else if (setting2==1)
					{
						rightcf.flowL2U = 0;
						rightcf.flowL2R = 0;
						rightcf.flowR2L = 0;
						rightcf.flowR2D = 0;
					}
					f[2] += rightcf.flowU2L+rightcf.flowR2L+rightcf.flowD2L + flowAdd.get(cross.id)[2]/2;
				}else{
					f[2] += flowAdd.get(cross.id)[2];
				}
			}
			if ( cross.neighbours[3].compareTo(Constants.LIGHT_NONE)!=0)
			{
				if(this.crosses.get(cross.neighbours[3])!=null){
					TrafficCrossroad DownCross =this.crosses.get(cross.neighbours[3]);
					CrossFlow downcf = Algorithms.CalcCrossFlow(DownCross.flowHistory.get(time),DownCross);
					int setting2 = DownCross.lightSettingHistory[time];
					if ( setting2==0)
					{
						downcf.flowD2L = 0;
						downcf.flowD2U = 0;
						downcf.flowU2D = 0;
						downcf.flowU2R = 0;
					}
					else if (setting2==1)
					{
						downcf.flowL2U = 0;
						downcf.flowL2R = 0;
						downcf.flowR2L = 0;
						downcf.flowR2D = 0;
					}
					f[3] += downcf.flowU2D+downcf.flowR2D+downcf.flowL2D +flowAdd.get(cross.id)[3]/2;
				}else{
					f[3] += flowAdd.get(cross.id)[3];
				}
			}

			flow.put(cross.id, f);
			
		}
		
		return flow;
	}
	/***
	 *输出当前地图流量
	 * 
	 * @param time
	 * @return
	 */
	public void GraphToString(){
		for(TrafficCrossroad cross : this.crosses.values()){
			String outputstr="ID:"+cross.id + " " +cross.neighbours[0]+ " " +cross.neighbours[1]+ " " +cross.neighbours[2]+ " " +cross.neighbours[3];
			System.out.println(outputstr);
		}
	}
	

}
