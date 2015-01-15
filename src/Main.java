import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Main 
{
	/***
	 * 灏嗚緭鍏ョ殑瀛楃涓茶浆鎹负娴侀噺map
	 * 
	 * @param traffic - 浜ら�氱粨鏋勫浘
	 * @param line - 杈撳嚭瀛楃涓�
	 * @return 娴侀噺map
	 */
	public static Map<String,int[]> String2Flow(TrafficGraph traffic, String line)
	{
		Map<String,int[]> ret = new HashMap<String,int[]>();
		
		for(String id : traffic.crosses.keySet())
		{
			ret.put(id, new int[4]);
		}
		
		String[] parts = line.split(";");
		
		for(String part : parts)
		{
			String[] pp = part.split(",");
			String id = pp[0];
			String frmId = pp[1];
			int flow = Integer.parseInt(pp[2]);
			
			TrafficCrossroad cr = traffic.crosses.get(id);
			
			for(int i=0;i<cr.neighbours.length;i++)
			{
				if (cr.neighbours[i].compareTo(frmId)==0)
				{
					ret.get(id)[i] = flow;
				}
			}
		}
		
		return ret;
	}
	
	/***
	 * 灏嗚矾鍙ｇ殑绾㈢豢鐏姸鎬佽浆鎹负瀛楃涓�
	 * 
	 * @param traffic - 浜ら�氱粨鏋勫浘
	 * @param time - 褰撳墠鏃堕棿
	 * @return 瀛楃涓�
	 */
	public static String OutputLightSetting(TrafficGraph traffic, int time)
	{
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		for(Map.Entry<String, TrafficCrossroad> entry : traffic.crosses.entrySet())
		{
			String cid = entry.getKey();
			TrafficCrossroad cross = entry.getValue();
			
			int setting = cross.lightSettingHistory[time];
			
			int[] status = new int[12];
			if ( setting == 0)
			{//姘村钩鏂瑰悜
				status[0] = 1;
				status[1] = 1;
				status[2] = 1;
				status[3] = 0;
				status[4] = 1;
				status[5] = 0;
				status[6] = 1;
				status[7] = 1;
				status[8] = 1;
				status[9] = 0;
				status[10] = 1;
				status[11] = 0;
			}
			else
			{//鍨傜洿鏂瑰悜
				status[0] = 0;
				status[1] = 1;
				status[2] = 0;
				status[3] = 1;
				status[4] = 1;
				status[5] = 1;
				status[6] = 0;
				status[7] = 1;
				status[8] = 0;
				status[9] = 1;
				status[10] = 1;
				status[11] = 1;				
			}
			
			for(int i=0;i<4;i++)
			{
				String dstId = cross.neighbours[i];
				if(dstId.compareTo(Constants.LIGHT_NONE)!=0)
				{
					if ( cnt> 0)
					{
						sb.append(";");
					}
					cnt++;
					sb.append(cid + "," + dstId);
					for(int j=0;j<3;j++)
					{
						sb.append("," + status[i*3+j]);
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	public static String Process(String line, int time, TrafficGraph traffic)
	{
		Map<String,int[]> flow = String2Flow(traffic,line);
		Algorithms.Solve(traffic, flow, time);
		
		return OutputLightSetting(traffic,time);	
	}
	
	public static void selfTest(TrafficGraph traffic ,FileOutputStream outfile) throws IOException{
		Map<String,int[]> initFlow = new HashMap<String,int[]>();
		OutputStreamWriter outputwriter =new OutputStreamWriter(outfile);
		outputwriter.write("");
		for(Map.Entry<String,TrafficCrossroad> entry:traffic.crosses.entrySet()){
			TrafficCrossroad cross = entry.getValue();
			initFlow.put(cross.id, cross.flowAdd[0]);
		}
		
		for(int i=0;i<Constants.MAX_TIME-1;i++){

			Algorithms.Solve(traffic, initFlow, i);
			String light =OutputLightSetting(traffic,i);
			outputwriter.append(light);
			outputwriter.append("\n");
//			outfile.write();
			if(i+1<Constants.MAX_TIME-1){
				initFlow=traffic.computeNextFlow(i);
			}
		}
		outputwriter.flush();
		outputwriter.close();
	}
	
	public static void main(String args[]) throws IOException
	{
		//initialize
		TrafficGraph traffic = new TrafficGraph();
		FileOutputStream outfile = new FileOutputStream(Constants.FILENAME_OUTPUT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				Main.class.getResourceAsStream(Constants.FILENAME_TRAFFIC)));
		BufferedReader readerFlow = new BufferedReader( new InputStreamReader(
				Main.class.getResourceAsStream(Constants.FILENAME_FLOW_ADD)));
		//璇诲叆绾㈢豢鐏殑缁撴瀯鍥�
		traffic.load(reader);
		//璇诲叆姣忎釜鏃跺埢绐佺劧鍑虹幇鐨勬祦閲�
		traffic.loadFlowAdd(readerFlow);
		reader.close();
		readerFlow.close();
		
		//main process
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String flows_str = br.readLine();
		int time=0;
//		TrafficCrossroad test1=new TrafficCrossroad("test1");
//		test1.setNeightbours("t1", "t2", "t3", "t4");
//		int []flowtest= new int[4];
//		for(int i=0;i<4;i++){
//			flowtest[i]=20;
//		}
//		CrossFlow result=Algorithms.CalcCrossFlow(flowtest, test1);
//		System.out.println(result.flowD2R);
		selfTest(traffic,outfile);
		System.out.println(traffic.penalty+" "+traffic.TimeoutTimes+" "+traffic.flowAway+" "+traffic.Total);
//		traffic.GraphToString();
		System.out.println("begin"+"happy");
//		while(!"end".equalsIgnoreCase(flows_str))
//		{
//			//TODO  浣犵殑浠ｇ爜,娉ㄦ剰锛屾暟鎹緭鍑洪渶淇濊瘉涓�琛岃緭鍑猴紝闄や簡鏁版嵁缁撴灉锛岃涓嶈灏嗕换浣曟棤鍏虫暟鎹垨寮傚父鎵撳嵃杈撳嚭
//			System.out.println(Process(flows_str,time,traffic));
//	        //鑾峰彇涓嬩竴涓椂闂存鐨勬祦閲忋�併�併��
//			flows_str = br.readLine();
//			time++;
//		}
		
		
		//鑷垜娴嬭瘯閮ㄥ垎浠ｇ爜
	}
}
