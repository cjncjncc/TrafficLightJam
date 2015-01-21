package questions1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int []A={5,6,7};
		int []B={1,2,3,4};
		double reslt=findMedianSortedArrays(A,B);
		System.out.println(reslt);
	}
	   public static double findMedianSortedArrays(int A[], int B[]) {
	        double ret;
	        int Abegin=0;
	        int Aend=A.length-1;
	        int Bbegin=0;
	        int Bend=B.length-1;
	        int Amid=findMid(Abegin,Aend);
	        int Bmid=findMid(Bbegin,Bend);
	        
	        if(A.length>2&&B.length>2){
	            while((A[Amid]!=B[Bmid])&&(!(Abegin==Aend)&&(Bbegin==Bend))){
	            	 System.out.println(Amid+" "+Bmid);
	                if(A[Amid]>B[Bmid]){
	                    Aend=Amid;
	                    Bbegin=Bmid;
	                    Amid=findMid(Abegin,Aend);
	                    Bmid=findMid(Bbegin,Bend);
	                }else if(A[Amid]<B[Bmid]){
	                    Bend=Bmid;
                    	Abegin=Amid;
	                    Amid=findMid(Abegin,Aend);
	                    Bmid=findMid(Bbegin,Bend);
	                }
	              
	            }
	            
	            int Aleft=Amid;
	            int Bleft=Bmid;
	            System.out.println(Amid+" "+Bmid);
	            if((A.length+B.length)%2==0){
	            	if((Aleft+Bleft)==(A.length+B.length)/2){
	            		ret=((double)(A[Amid])+(double)(B[Bmid]))/2;
	            	}else{
	            		if(Amid<A.length-1&&Bmid<B.length-1){
		            		int newArray[]=new int[4];
		            		newArray[0]=A[Amid];
		            		newArray[1]=A[Amid+1];
		            		newArray[2]=B[Bmid+1];
		            		newArray[3]=B[Bmid];
		            		Arrays.sort(newArray);
		            		ret=solveOne(newArray);
	            		}else{
	            			if(Amid==A.length-1&&Bmid==B.length-1){
	            				ret=((double)(A[Amid])+(double)(B[Bmid]))/2;
	            			}else{
	            				if(Amid==A.length-1){
	            					int newArray[]=new int[4];
	    		            		newArray[0]=A[Amid];
	    		            		newArray[1]=B[Bmid-1];
	    		            		newArray[2]=B[Bmid+1];
	    		            		newArray[3]=B[Bmid];
	    		            		Arrays.sort(newArray);
	    		            		ret=solveOne(newArray);
	            				}else{
	            					int newArray[]=new int[4];
	    		            		newArray[0]=B[Bmid];
	    		            		newArray[1]=A[Amid-1];
	    		            		newArray[2]=A[Amid+1];
	    		            		newArray[3]=A[Amid];
	    		            		Arrays.sort(newArray);
	    		            		ret=solveOne(newArray);
	            				}
	            			}
	            		}
	            	}
	            }else{
	            	if((Aleft+Bleft)==(A.length+B.length)/2){
	            		ret=Math.min(A[Amid], B[Bmid]);
	            	}else{
	            		if((Aleft+Bleft)==(A.length+B.length)/2-2){
	            			int maxAB=Math.max(A[Amid],B[Bmid]);
	            			int minABadd1=Math.min(A[Amid+1], B[Bmid+1]);
	            			ret=Math.max(maxAB,minABadd1);
	            		}else{
	            			if(A[Amid]>B[Bmid]){
	            				ret=A[Amid];
			       
	            			}else {
	            				ret=B[Bmid];
			            
	            			}
	            		}
	            	}
	            }
	            
	        }else{
	            int newArray[]=new int[A.length+B.length];
	            int number=0;
	            for(int i:A){
	            	newArray[number]=i;
	            	number++;
	            }
	            for(int i:B){
	            	newArray[number]=i;
	            	number++;
	            }
	            Arrays.sort(newArray);
	            ret=solveOne(newArray);
	        }
	        return ret;
	    }
	    public static int findMid(int min,int max){
	        int add=(max-min)/2;
	        int ret=min+add;
	        return ret;
	    }
	    public static double solveOne(int []A){
	        double ret;
	        int mid=findMid(0,A.length-1);
	        if(A.length%2==0){
	            ret=((double)(A[mid])+(double)(A[mid+1]))/2;
	        }else{
	            ret=(double)(A[mid]);
	        }
	        return ret;
	    }
}
