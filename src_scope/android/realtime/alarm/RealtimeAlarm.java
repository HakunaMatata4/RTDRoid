package android.realtime.alarm;

import javax.realtime.ImmortalMemory;

public class RealtimeAlarm {
	
	private long timestamp = -1;
	private int priority = 0;
	private PendingIntent operation = null;
	private boolean isRepeatable = false;
	private long repeatInterval = 0;
	
	private static int MAX_SIZE = 100;
	private static int REMAINING_SIZE = 100;
	private static boolean isSizeSet = false;
	private static boolean inSubtree = false;
	private static boolean hasIterated = false;
	
	private RealtimeAlarm parent = null;
	private RealtimeAlarm leftLink = null;
	private RealtimeAlarm rightLink = null;
	private RealtimeAlarm downLink = null;
	private RealtimeAlarm backLink = null;
	
	private RealtimeAlarm auxParent = null;
	private RealtimeAlarm auxLeftLink = null;
	private RealtimeAlarm auxRightLink = null;
	private RealtimeAlarm auxDownLink = null;
	private RealtimeAlarm auxBackLink = null;
	
	private static RealtimeAlarm lever = null;
	private static RealtimeAlarm nextTimestamp = null;
	private static RealtimeAlarm root = null;
	private static RealtimeAlarm auxRoot = null;
	private static RealtimeAlarm pool = null;
	private static RealtimeAlarm fixedRoot = new RealtimeAlarm(-1, 0, null);
	
	public static void fillPool(final int size){
		isSizeSet = true;
		MAX_SIZE = size;
		REMAINING_SIZE = size;
		ImmortalMemory.instance().enter(new Runnable(){
			public void run() {
				for(int i=0; i<size; i++){
					RealtimeAlarm alarm = new RealtimeAlarm(-1, 0, null);
					if(pool == null) pool = alarm;
					else{
						RealtimeAlarm next = pool;
						alarm.backLink = next;
						next.parent = alarm;
						pool = alarm;
					}
				}
			}
		});
	}
	
	public static int getPoolSize(){
		return REMAINING_SIZE;
	}
	
	public static void recycle(RealtimeAlarm alarm){
		synchronized(pool){
			REMAINING_SIZE = REMAINING_SIZE + 1;
			alarm.reset();
			alarm.setParameters(-1, 0, null);
			if(pool == null) pool = alarm;
			else{
				RealtimeAlarm next = pool;
				alarm.backLink = next;
				next.parent = alarm;
				pool = alarm;
			}
		}
	}
	
	public static RealtimeAlarm fetch(long timestamp, int priority, PendingIntent operation){
		if(isSizeSet == false){
			isSizeSet = true;
			fillPool(MAX_SIZE);
		}
		synchronized(pool){
			if(pool == null){
				RealtimeAlarm alarm = auxRoot;
				while(true){
					if(alarm.auxLeftLink == null) break;
					else alarm = alarm.auxLeftLink;
				}
				if(alarm.priority < priority){
					root.delete(alarm.timestamp, alarm.priority, false);
					alarm = alarm.setParameters(timestamp, priority, operation).reset();
					return alarm;
				}
				else throw new IllegalArgumentException("Alarm cannot be created.");
			}
			else{
				RealtimeAlarm alarm = pool;
				pool = pool.backLink;
				if(pool != null) pool.parent = null;
				REMAINING_SIZE = REMAINING_SIZE - 1;
				return alarm.setParameters(timestamp, priority, operation).reset();
			}
		}
	}
	
	public RealtimeAlarm(long timestamp, int priority, PendingIntent operation){
		setParameters(timestamp, priority, operation);
	}
	
	public RealtimeAlarm setParameters(long timestamp, int priority, PendingIntent operation){
		this.timestamp = timestamp;
		this.priority = priority;
		this.operation = operation;
		this.isRepeatable = false;
		this.repeatInterval = 0;
		return this;
	}
	
	public RealtimeAlarm setRepeatable(long repeatInterval){
		this.isRepeatable = true;
		this.repeatInterval = repeatInterval;
		return this;
	}
	
	public static void setRoot(RealtimeAlarm alarm){
		root = alarm;
	}
	
	public static RealtimeAlarm getRoot(){
		if(root == null) setRoot(fixedRoot);
		return root;
	}
	
	public static void setAuxRoot(RealtimeAlarm alarm){
		auxRoot = alarm;
	}
	
	public static RealtimeAlarm getAuxRoot(){
		if(auxRoot == null) setAuxRoot(fixedRoot);
		return auxRoot;
	}
	
	public static RealtimeAlarm getFixedRoot(){
		return fixedRoot;
	}
	
	public long getTimeStamp(){
		return timestamp;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public PendingIntent getActivity(){
		return operation;
	}
	
	public boolean isRepeatable(){
		return isRepeatable;
	}
	
	public long getRepeatInterval(){
		return repeatInterval;
	}
	
	public static RealtimeAlarm getNextTimestamp(){
		if(lever == null && root != null && !root.equals(fixedRoot)) findLever();
		return lever;
	}
	
	public static RealtimeAlarm getNext(){
		RealtimeAlarm alarm = lever;
		if(lever != null && hasIterated == true){
			root.delete(lever.timestamp, lever.priority, true);
			lever = null;
		}
		else if(lever != null && hasIterated == false){
			if(lever.downLink != null){
				inSubtree = true;
				lever = lever.downLink;
			}
			else{
				root.delete(lever.timestamp, lever.priority, true);
				lever = null;
			}
			if(inSubtree == true){
				while(true){
					if(lever.rightLink != null) lever = lever.rightLink;
					else break;
				}
				alarm = lever;
				lever = lever.parent;
				if(lever.equals(nextTimestamp)) hasIterated = true;
				root.delete(alarm.timestamp, alarm.priority, true);
			}
		}
		return alarm;
	}
	
	public RealtimeAlarm getNextInList(){
		return this.backLink;
	}
	
	private static void findLever(){
		synchronized(root){
			RealtimeAlarm next = root;
			while(true){
				if(next.leftLink != null) next = next.leftLink;
				else{
					lever = next;
					nextTimestamp = next;
					inSubtree = false;
					hasIterated = false;
					break;
				}
			}
		}
	}
	
	private void resetLinks(){
		this.leftLink = null;
		this.rightLink = null;
		this.backLink = null;
		this.downLink = null;
		this.parent = null;
	}
	
	private void resetAuxLinks(){
		this.auxLeftLink = null;
		this.auxRightLink = null;
		this.auxBackLink = null;
		this.auxDownLink = null;
		this.auxParent = null;
	}
	
	private RealtimeAlarm reset(){
		resetLinks();
		resetAuxLinks();
		return this;
	}
	
	private RealtimeAlarm resetLever(RealtimeAlarm alarm){
		lever = null;
		nextTimestamp = null;
		inSubtree = false;
		hasIterated = false;
		return this;
	}
	
	public void insert(RealtimeAlarm alarm){
		synchronized(root){
			RealtimeAlarm.getRoot().insertPrime(alarm);
			RealtimeAlarm.getAuxRoot().insertAux(alarm);
			this.resetLever(alarm);
		}
	}
	
	public RealtimeAlarm delete(long timestamp){
		synchronized(root){
			RealtimeAlarm alarm = RealtimeAlarm.getRoot().deletePrime(timestamp);
			alarm.iterateAndDelete(alarm);
			return alarm;
		}
	}
	
	public RealtimeAlarm delete(long timestamp, int priority, boolean isAll){
		synchronized(root){
			RealtimeAlarm alarm = RealtimeAlarm.getRoot().deletePrime(timestamp, priority, isAll);
			RealtimeAlarm.getAuxRoot().deleteAux(timestamp, priority, isAll);
			return alarm;
		}
	}
	
	private void insertPrime(RealtimeAlarm alarm){
		if(root.equals(fixedRoot)) setRoot(alarm);
		else{
			if(alarm.timestamp < this.timestamp){
				if(this.leftLink == null){
					this.leftLink = alarm;
					alarm.parent = this;
				}
				else this.leftLink.insertPrime(alarm);
			}
			else if(alarm.timestamp > this.timestamp){
				if(this.rightLink == null){
					this.rightLink = alarm;
					alarm.parent = this;
				}
				else this.rightLink.insertPrime(alarm);
			}
			else{
				if(this.priority == alarm.priority) this.insertIntoList(alarm);
				else if(alarm.priority < this.priority){
					alarm = this.swapAlarms(alarm);
					if(this.equals(root)) setRoot(alarm);
					alarm.insertPrime(this);
				}
				else{
					if(this.downLink == null){
						this.downLink = alarm;
						alarm.parent = this;
					}
					else this.downLink.insertIntoSubtree(alarm);
				}
			}
		}
	}
	
	private void insertIntoSubtree(RealtimeAlarm alarm){
		if(alarm.priority < this.priority){
			if(this.leftLink == null){
				this.leftLink = alarm;
				alarm.parent = this;
			}
			else this.leftLink.insertIntoSubtree(alarm);
		}
		else if(alarm.priority > this.priority){
			if(this.rightLink == null){
				this.rightLink = alarm;
				alarm.parent = this;
			}
			else this.rightLink.insertIntoSubtree(alarm);
		}
		else{
			if(this.backLink == null){
				this.backLink = alarm;
				alarm.parent = this;
			}
			else this.backLink.insertIntoList(alarm);
		}
	}
	
	private void insertIntoList(RealtimeAlarm alarm){
		if(this.backLink == null){
			this.backLink = alarm;
			alarm.parent = this;
		}
		else this.backLink.insertIntoList(alarm);
	}
	
	private RealtimeAlarm deletePrime(long timestamp){
		RealtimeAlarm result = get(timestamp);
		return deleteHelper(result);
	}
	
	private void iterateAndDelete(RealtimeAlarm result){
		if(auxRoot != null) auxRoot.deleteAux(this.timestamp, this.priority, true);
		if(this.downLink != null){
			if(this.downLink.leftLink != null) this.downLink.leftLink.iterateAndDelete(result);
			this.downLink.iterateAndDelete(result);
			if(this.downLink.rightLink != null) this.downLink.rightLink.iterateAndDelete(result);
		}
		else if(!this.equals(result)){
			if(this.leftLink != null) this.leftLink.iterateAndDelete(result);
			if(this.rightLink != null) this.rightLink.iterateAndDelete(result);
		}
	}
	
	private RealtimeAlarm deletePrime(RealtimeAlarm alarm){
		return deleteHelper(alarm);
	}
	
	private RealtimeAlarm deleteHelper(RealtimeAlarm result){
		if(result == null) return null;
		if(result.parent == null){
			if(this.leftLink != null){
				RealtimeAlarm.setRoot(this.leftLink);
				if(result.rightLink != null) result.leftLink.insertPrime(result.rightLink);
				this.leftLink.parent = null;
				this.leftLink = null;
				this.rightLink = null;
			}
			else if(this.rightLink != null){
				RealtimeAlarm.setRoot(this.rightLink);
				if(result.leftLink != null) result.rightLink.insertPrime(result.leftLink);
				this.rightLink.parent = null;
				this.leftLink = null;
				this.rightLink = null;
			}
			else{
				RealtimeAlarm.setRoot(null);
			}
		}
		else{
			if(result.equals(result.parent.leftLink)){
				if(result.leftLink != null){
					result.parent.leftLink = result.leftLink;
					result.leftLink.parent = result.parent;
					if(result.rightLink != null) result.leftLink.insertPrime(result.rightLink);
				}
				else{
					result.parent.leftLink = result.rightLink;
					if(result.rightLink != null) result.rightLink.parent = result.parent;
					if(result.leftLink != null) result.rightLink.insertPrime(result.leftLink);
				}
			}
			else if(result.equals(result.parent.rightLink)){
				if(result.leftLink != null){
					result.parent.rightLink = result.leftLink;
					result.leftLink.parent = result.parent;
					if(result.rightLink != null) result.leftLink.insertPrime(result.rightLink);
				}
				else{
					result.parent.rightLink = result.rightLink;
					if(result.rightLink != null) result.rightLink.parent = result.parent;
					if(result.leftLink != null) result.rightLink.insertPrime(result.leftLink);
				}
			}
			else{
				if(result.leftLink != null){
					result.parent.downLink = result.leftLink;
					result.leftLink.parent = result.parent;
					if(result.rightLink != null) result.leftLink.insertPrime(result.rightLink);
				}
				else{
					result.parent.downLink = result.rightLink;
					if(result.rightLink != null) result.rightLink.parent = result.parent;
					if(result.leftLink != null) result.rightLink.insertPrime(result.leftLink);
				}
			}
		}
		return result;
	}
	
	public RealtimeAlarm get(long timestamp){
		if(timestamp < this.timestamp){
			if(this.leftLink == null) return null;
			else return this.leftLink.get(timestamp);
		}
		else if(timestamp > this.timestamp){
			if(this.rightLink == null) return null;
			else return this.rightLink.get(timestamp);
		}
		else{
			return this;
		}
	}

	private RealtimeAlarm deletePrime(long timestamp, int priority, boolean isAll){
		RealtimeAlarm alarm = get(timestamp);
		return alarm.deletePriority(priority, alarm, isAll);
	}
	
	private RealtimeAlarm deletePriority(int priority, RealtimeAlarm alarm, boolean isAll){
		 if(priority == this.priority){
			 RealtimeAlarm returnedAlarm = null;
			 if(this.equals(alarm)){
				 if(this.downLink == null){
					 returnedAlarm = deletePrime(this);
				 }
				 else{
					 RealtimeAlarm downLink = this.downLink;
					 while(true){
						 if(downLink.rightLink == null) break;
						 else downLink = downLink.rightLink;
					 }
					 downLink = this.deleteHelper(downLink);
					 downLink = this.swapAlarms(downLink);
					 if(this.equals(root)) setRoot(downLink);
					 returnedAlarm = this;
				 }
			 }
			 else{
				 returnedAlarm = this.parent.deleteHelper(this);
			 }
			 if(!isAll){
				 if(returnedAlarm.backLink != null){
					 RealtimeAlarm pushedAlarm = returnedAlarm.backLink;
					 pushedAlarm.parent = null;
					 RealtimeAlarm.getRoot().insertPrime(pushedAlarm);
				 }
				 returnedAlarm.resetLinks();
			 }
			 return returnedAlarm;
		 }
		 if(this.equals(alarm)){
			 if(this.downLink == null) return null;
			 else return this.downLink.deletePriority(priority, alarm, isAll);
		 }
		 if(priority < this.priority){
			 if(this.leftLink == null) return null;
			 else return this.leftLink.deletePriority(priority, alarm, isAll);
		 }
		 else{
			 if(this.rightLink == null) return null;
			 else return this.rightLink.deletePriority(priority, alarm, isAll); 
		 }
	}
	
	private void insertAux(RealtimeAlarm alarm){
		if(auxRoot.equals(fixedRoot)) setAuxRoot(alarm);
		else{
			if(alarm.priority < this.priority){
				if(this.auxLeftLink == null){
					this.auxLeftLink = alarm;
					alarm.auxParent = this;
				}
				else this.auxLeftLink.insertAux(alarm);
			}
			else if(alarm.priority > this.priority){
				if(this.auxRightLink == null){
					this.auxRightLink = alarm;
					alarm.auxParent = this;
				}
				else this.auxRightLink.insertAux(alarm);
			}
			else{
				if(this.timestamp == alarm.timestamp) this.insertIntoListAux(alarm);
				else if(alarm.timestamp > this.timestamp){
					alarm = this.swapAuxAlarms(alarm);
					if(this.equals(auxRoot)) setAuxRoot(alarm);
					alarm.insertAux(this);
				}
				else{
					if(this.auxDownLink == null){
						this.auxDownLink = alarm;
						alarm.auxParent = this;
					}
					else this.auxDownLink.insertIntoSubtreeAux(alarm);
				}
			}
		}
	}
	
	private void insertIntoSubtreeAux(RealtimeAlarm alarm){
		if(alarm.timestamp < this.timestamp){
			if(this.auxLeftLink == null){
				this.auxLeftLink = alarm;
				alarm.auxParent = this;
			}
			else this.auxLeftLink.insertIntoSubtreeAux(alarm);
		}
		else if(alarm.timestamp > this.timestamp){
			if(this.auxRightLink == null){
				this.auxRightLink = alarm;
				alarm.auxParent = this;
			}
			else this.auxRightLink.insertIntoSubtreeAux(alarm);
		}
		else{
			if(this.auxBackLink == null){
				this.auxBackLink = alarm;
				alarm.auxParent = this;
			}
			else this.auxBackLink.insertIntoListAux(alarm);
		}
	}
	
	private void insertIntoListAux(RealtimeAlarm alarm){
		if(this.auxBackLink == null){
			this.auxBackLink = alarm;
			alarm.auxParent = this;
		}
		else this.auxBackLink.insertIntoListAux(alarm);
	}
	
	@SuppressWarnings("unused")
	private RealtimeAlarm deleteAux(int priority){
		RealtimeAlarm result = getAux(priority);
		return deleteHelperAux(result);
	}
	
	private RealtimeAlarm deleteAux(RealtimeAlarm alarm){
		return deleteHelperAux(alarm);
	}
	
	private RealtimeAlarm deleteHelperAux(RealtimeAlarm result){
		if(result == null) return null;
		if(result.auxParent == null){
			if(this.auxLeftLink != null){
				RealtimeAlarm.setAuxRoot(this.auxLeftLink);
				if(result.auxRightLink != null) result.auxLeftLink.insertAux(result.auxRightLink);
				this.auxLeftLink.auxParent = null;
				this.auxLeftLink = null;
				this.auxRightLink = null;
			}
			else if(this.auxRightLink != null){
				RealtimeAlarm.setAuxRoot(this.auxRightLink);
				if(result.auxLeftLink != null) result.auxRightLink.insertAux(result.auxLeftLink);
				this.auxRightLink.auxParent = null;
				this.auxLeftLink = null;
				this.auxRightLink = null;
			}
			else{
				RealtimeAlarm.setAuxRoot(null);
			}
		}
		else{
			if(result.equals(result.auxParent.auxLeftLink)){
				if(result.auxLeftLink != null){
					result.auxParent.auxLeftLink = result.auxLeftLink;
					result.auxLeftLink.auxParent = result.auxParent;
					if(result.auxRightLink != null) result.auxLeftLink.insertAux(result.auxRightLink);
				}
				else{
					result.auxParent.auxLeftLink = result.auxRightLink;
					if(result.auxRightLink != null) result.auxRightLink.auxParent = result.auxParent;
					if(result.auxLeftLink != null) result.auxRightLink.insertAux(result.auxLeftLink);
				}
			}
			else if(result.equals(result.auxParent.auxRightLink)){
				if(result.auxLeftLink != null){
					result.auxParent.auxRightLink = result.auxLeftLink;
					result.auxLeftLink.auxParent = result.auxParent;
					if(result.auxRightLink != null) result.auxLeftLink.insertAux(result.auxRightLink);
				}
				else{
					result.auxParent.auxRightLink = result.auxRightLink;
					if(result.auxRightLink != null) result.auxRightLink.auxParent = result.auxParent;
					if(result.auxLeftLink != null) result.auxRightLink.insertAux(result.auxLeftLink);
				}
			}
			else{
				if(result.auxLeftLink != null){
					result.auxParent.auxDownLink = result.auxLeftLink;
					result.auxLeftLink.auxParent = result.auxParent;
					if(result.auxRightLink != null) result.auxLeftLink.insertAux(result.auxRightLink);
				}
				else{
					result.auxParent.auxDownLink = result.auxRightLink;
					if(result.auxRightLink != null) result.auxRightLink.auxParent = result.auxParent;
					if(result.auxLeftLink != null) result.auxRightLink.insertAux(result.auxLeftLink);
				}
			}
		}
		return result;
	}
	
	public RealtimeAlarm getAux(int priority){
		if(priority < this.priority){
			if(this.auxLeftLink == null) return null;
			else return this.auxLeftLink.getAux(priority);
		}
		else if(priority > this.priority){
			if(this.auxRightLink == null) return null;
			else return this.auxRightLink.getAux(priority);
		}
		else{
			return this;
		}
	}

	private RealtimeAlarm deleteAux(long timestamp, int priority, boolean isAll){
		RealtimeAlarm alarm = getAux(priority);
		return alarm.deletePriorityAux(timestamp, alarm, isAll);
	}
	
	private RealtimeAlarm deletePriorityAux(long timestamp, RealtimeAlarm alarm, boolean isAll){
		 if(timestamp == this.timestamp){
			 RealtimeAlarm returnedAlarm = null;
			 if(this.equals(alarm)){
				 if(this.auxDownLink == null){
					 returnedAlarm = deleteAux(this);
				 }
				 else{
					 RealtimeAlarm downLink = this.auxDownLink;
					 while(true){
						 if(downLink.auxRightLink == null) break;
						 else downLink = downLink.auxRightLink;
					 }
					 downLink = this.deleteHelperAux(downLink);
					 downLink = this.swapAuxAlarms(downLink);
					 if(this.equals(auxRoot)) setAuxRoot(downLink);
					 returnedAlarm = this;
				 }
			 }
			 else{
				 returnedAlarm = this.auxParent.deleteHelperAux(this);
			 }
			 if(!isAll){
				 if(returnedAlarm.auxBackLink != null){
					 RealtimeAlarm pushedAlarm = returnedAlarm.auxBackLink;
					 pushedAlarm.auxParent = null;
					 RealtimeAlarm.getAuxRoot().insertAux(pushedAlarm);
				 }
				 returnedAlarm.resetAuxLinks();
			 }
			 return returnedAlarm;
		 }
		 if(this.equals(alarm)){
			 if(this.auxDownLink == null) return null;
			 else return this.auxDownLink.deletePriorityAux(timestamp, alarm, isAll);
		 }
		 if(timestamp < this.timestamp){
			 if(this.auxLeftLink == null) return null;
			 else return this.auxLeftLink.deletePriorityAux(timestamp, alarm, isAll);
		 }
		 else{
			 if(this.auxRightLink == null) return null;
			 else return this.auxRightLink.deletePriorityAux(timestamp, alarm, isAll); 
		 }
	}
	
	private RealtimeAlarm swapAlarms(RealtimeAlarm alarm){
		synchronized (this){
			alarm.leftLink = this.leftLink;
			alarm.rightLink = this.rightLink;
			alarm.downLink = this.downLink;
			alarm.parent = this.parent;
			if(this.leftLink != null) this.leftLink.parent = alarm;
			if(this.rightLink != null) this.rightLink.parent = alarm;
			if(this.downLink != null) this.downLink.parent = alarm;
			this.leftLink = null;
			this.rightLink = null;
			this.downLink = null;
			if(this.parent != null){
				if(this.parent.leftLink != null && this.parent.leftLink.equals(this)) this.parent.leftLink = alarm;
				else this.parent.rightLink = alarm;
				this.parent = null;
			}
			return alarm;
		}
	}
	
	private RealtimeAlarm swapAuxAlarms(RealtimeAlarm alarm){
		synchronized (this){
			alarm.auxLeftLink = this.auxLeftLink;
			alarm.auxRightLink = this.auxRightLink;
			alarm.auxDownLink = this.auxDownLink;
			alarm.auxParent = this.auxParent;
			if(this.auxLeftLink != null) this.auxLeftLink.auxParent = alarm;
			if(this.auxRightLink != null) this.auxRightLink.auxParent = alarm;
			if(this.auxDownLink != null) this.auxDownLink.auxParent = alarm;
			this.auxLeftLink = null;
			this.auxRightLink = null;
			this.auxDownLink = null;
			if(this.auxParent != null){
				if(this.auxParent.auxLeftLink != null && this.auxParent.auxLeftLink.equals(this)) this.auxParent.auxLeftLink = alarm;
				else this.auxParent.auxRightLink = alarm;
				this.auxParent = null;
			}
			return alarm;
		}
	}
	
	public boolean equals(RealtimeAlarm alarm){
		if(alarm == null) return false;
		if(this.timestamp == alarm.timestamp && this.priority == alarm.priority){
			if(this.operation == null && alarm.operation == null) return true;
			else if(this.operation != null && alarm.operation != null && this.operation.hashCode() == alarm.operation.hashCode()) return true;
			else return false;
		}
		else return false;
	}
	
	public static void printTree(RealtimeAlarm alarm){
		if(alarm.leftLink != null) printTree(alarm.leftLink);
		System.out.println(alarm);
		if(alarm.backLink != null){
			System.out.println("##########--Linked List Start--##########");
			printSideTree(alarm.backLink);
			System.out.println("##########--Linked List End--##########");
		}
		if(alarm.downLink != null){
			System.out.println("%%%%%%%%%%%%%%%%%%%%--Sub Tree Start--%%%%%%%%%%%%%%%%%%%%");
			printDownTree(alarm.downLink);
			System.out.println("%%%%%%%%%%%%%%%%%%%%--Sub Tree End--%%%%%%%%%%%%%%%%%%%%");
		}
		System.out.println();
		if(alarm.rightLink != null) printTree(alarm.rightLink);
	}

	private static void printDownTree(RealtimeAlarm alarm) {
		if(alarm.leftLink != null) printDownTree(alarm.leftLink);
		System.out.println(alarm);
		if(alarm.backLink != null){
			System.out.println("##########--Linked List Start--##########");
			printSideTree(alarm.backLink);
			System.out.println("##########--Linked List End--##########");
		}
		if(alarm.rightLink != null) printDownTree(alarm.rightLink);
	}

	private static void printSideTree(RealtimeAlarm alarm) {
		System.out.println(alarm);
		if(alarm.backLink != null) printSideTree(alarm.backLink);
	}
	
	public static void printAuxTree(RealtimeAlarm alarm){
		if(alarm.auxLeftLink != null) printAuxTree(alarm.auxLeftLink);
		System.out.println(alarm);
		if(alarm.auxBackLink != null){
			System.out.println("##########--Linked List Start--##########");
			printAuxSideTree(alarm.auxBackLink);
			System.out.println("##########--Linked List End--##########");
		}
		if(alarm.auxDownLink != null){
			System.out.println("%%%%%%%%%%%%%%%%%%%%--Sub Tree Start--%%%%%%%%%%%%%%%%%%%%");
			printAuxDownTree(alarm.auxDownLink);
			System.out.println("%%%%%%%%%%%%%%%%%%%%--Sub Tree End--%%%%%%%%%%%%%%%%%%%%");
		}
		System.out.println();
		if(alarm.auxRightLink != null) printAuxTree(alarm.auxRightLink);
	}

	private static void printAuxDownTree(RealtimeAlarm alarm) {
		if(alarm.auxLeftLink != null) printAuxDownTree(alarm.auxLeftLink);
		System.out.println(alarm);
		if(alarm.auxBackLink != null){
			System.out.println("##########--Linked List Start--##########");
			printAuxSideTree(alarm.auxBackLink);
			System.out.println("##########--Linked List End--##########");
		}
		if(alarm.auxRightLink != null) printAuxDownTree(alarm.auxRightLink);
	}

	private static void printAuxSideTree(RealtimeAlarm alarm) {
		System.out.println(alarm);
		if(alarm.auxBackLink != null) printAuxSideTree(alarm.auxBackLink);
	}
	
	public String toString(){
		String alarm = "";
		alarm += "Timestamp = " + timestamp + ", Priority = " + priority + ", Echo = " + operation;
		return alarm;
	}
}