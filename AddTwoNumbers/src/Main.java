
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ListNode n1=new ListNode(3);
		ListNode n2=new ListNode(2);
		ListNode n3=new ListNode(7);
//		n1.next=n2;
		ListNode result=addTwoNumbers(n1,n3);
		System.out.print(result.val);
		while(result.next!=null){
			result=result.next;
			System.out.print(result.val);
		}
	}
	   public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		   ListNode ret = null;
		   int val=l1.val+l2.val;
		   int add=val/10;
		   val=val%10;
		   ret=new ListNode(val);
		   if(l1.next!=null&&l2.next!=null){
			   ListNode  ret2 =addTwoNumbersWith(l1.next,l2.next,add);
			   ret.next=ret2;
		   }else if(l1.next!=null){
			   ret.next=addTwoNumbersWith(l1.next,null,add);
		   }else if(l2.next!=null){
			   ret.next=addTwoNumbersWith(l2.next,null,add);
		   }else if(add>0){
			   ret.next=addTwoNumbersWith(null,null,add);
		   }
		   return ret;
	      
	    }
	   public static ListNode addTwoNumbersWith(ListNode l1, ListNode l2,int Add) {
		   ListNode ret = null;
		   ListNode next=null;
		   ListNode nextl1=null;
		   ListNode nextl2=null;
		   int val=Add;
		   int add=0;
		   ret=new ListNode(val);
		   if(l1!=null){
			   val+=l1.val;
			   nextl1=l1.next;
		   }
		   if(l2!=null){
			   val+=l2.val;
			   nextl2=l2.next;
		   }
		   add=val/10;
		   val=val%10;
		   if(nextl1!=null||nextl2!=null||add>0){
			  next=addTwoNumbersWith(nextl1,nextl2,add);
		   }
		   ret =new ListNode(val);
		   ret.next=next;
		   return ret;
	      
	    }
	   
}
