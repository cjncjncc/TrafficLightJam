package questions1;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int []A={1,2};
		int []B={1,2};
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
	        if(A.length>0&&B.length>0){
	            while((A[Amid]!=B[Bmid])&&(Abegin!=Aend)&&(Bbegin!=Bend)){
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
	            System.out.println(Amid+" "+Bmid);
	            if(A[Amid]==B[Bmid]){
	                ret = A[Amid];
	            }else{
	                if((A.length+B.length)%2==0){
	                    ret=((double)A[Amid]+(double)B[Bmid])/2;
	                }else{
	                    int total=A.length-Amid+B.length-Bmid;
	                    if(total==(A.length+B.length)/2){
	                        ret=Math.max(A[Amid],B[Bmid]);
	                    }else{
	                        ret=Math.min(A[Amid],B[Bmid]);
	                    }
	                }
	            }
	        }else{
	            if(A.length>0){
	                ret=solveOne(A);
	            }else{
	                ret=solveOne(B);
	            }
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
