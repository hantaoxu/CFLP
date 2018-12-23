package solution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
public class Solution {
	private int facilityNum;
	private int customerNum;
	private List<Facility> facilityList;
	private List<Customer> customerList;
	private List<Instance> instanceList;
	// 注意路径的修改
	private static String Path = "XXXXX\\Instances";
	private static int INSTANCENUM = 71;
	public class Facility {
		int facilityId;
		int capacity;
		int cost;
		boolean open;
		// 从customerId -> cost的映射
		Map<Integer, Integer> assignmentCost;
		public int getFacilityId() {
			return facilityId;
		}
		public void setFacilityId(int facilityId) {
			this.facilityId = facilityId;
		}
		public int getCapacity() {
			return capacity;
		}
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
		public int getCost() {
			return cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		public Map<Integer, Integer> getAssignmentCost() {
			return assignmentCost;
		}
		public void setAssignmentCost(Map<Integer, Integer> assignmentCost) {
			this.assignmentCost = assignmentCost;
		}
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		public Facility(Facility faci) {
			super();
			this.facilityId = faci.facilityId;
			this.capacity = faci.capacity;
			this.cost = faci.cost;
			this.open = faci.open;
			this.assignmentCost = faci.assignmentCost;
		}
		public Facility() {}
	}
	public class Customer {
		int customerId;
		int demand;
		int assignedTo; // 去哪个设施
		public int getCustomerId() {
			return customerId;
		}
		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}
		public int getDemand() {
			return demand;
		}
		public void setDemand(int demand) {
			this.demand = demand;
		}
		public int getAssignedTo() {
			return assignedTo;
		}
		public void setAssignedTo(int assignedTo) {
			this.assignedTo = assignedTo;
		}
		public Customer(Customer cust) {
			super();
			this.customerId = cust.customerId;
			this.demand = cust.demand;
			this.assignedTo = cust.assignedTo;
		}
		public Customer() {}
	}
	public class Instance {
		int result;
		int time;
		String id;
		List<Boolean> openList;
		List<Integer> assignmentList;
		public int getResult() {
			return result;
		}
		public void setResult(int result) {
			this.result = result;
		}
		public int getTime() {
			return time;
		}
		public void setTime(int time) {
			this.time = time;
		}
		public List<Boolean> getOpenList() {
			return openList;
		}
		public void setOpenList(List<Boolean> openList) {
			this.openList = openList;
		}
		public List<Integer> getAssignmentList() {
			return assignmentList;
		}
		public void setAssignmentList(List<Integer> assignmentList) {
			this.assignmentList = assignmentList;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	}
	//读取文件内容，默认文件内容格式正确，不做检查
	public void ReadFileAndInit(String path) {
		File file = new File(path);
		//System.out.println(path);
		BufferedReader bReader = null;
		try {
			// 字符串相关
			String str;
			List<Integer> intList = null;
			bReader = new BufferedReader(new FileReader(file));

			// 读取设施和顾客数量
			str = bReader.readLine();
			intList = getNumberFromLine(str);
			facilityNum = intList.get(0).intValue();
			customerNum = intList.get(1).intValue();
			
			// 读取设施容量和开销
			for (int i = 0; i < facilityNum; i++) {
				str = bReader.readLine();
				intList = getNumberFromLine(str);
				Facility faci = new Facility();
				faci.setCapacity(intList.get(0).intValue());
				faci.setCost(intList.get(1).intValue());
				faci.setOpen(false);
				faci.setFacilityId(i);
				faci.setAssignmentCost(new HashMap<Integer, Integer>());
				facilityList.add(faci);
			}
			// 读取顾客的需求
			for (int i = 0; i < customerNum; ) {
				str = bReader.readLine();
				intList = getNumberFromLine(str);
				for (Integer tmp : intList) {
					Customer cust = new Customer();
					cust.setAssignedTo(-1);
					cust.setCustomerId(i);
					cust.setDemand(tmp);
					customerList.add(cust);
					i++;
				}
			}
			// 读取每个顾客到设施的开销
			for (int i = 0; i < facilityNum; i++) {
				for (int j = 0; j < customerNum; ) {
					str = bReader.readLine();
					intList = getNumberFromLine(str);
					Facility faci = facilityList.get(i);
					for (Integer tmp : intList) {
						faci.getAssignmentCost().put(new Integer(j), tmp);
						j++;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	public List<Integer> getNumberFromLine(String str) {
		// 替换.为空格，因为读取的文件内容格式可能不太统一
		String strCopy = str.replace(".", " ");
		strCopy = strCopy.trim();
		String[] strs = strCopy.split("\\s+");
		List<Integer> intList = new ArrayList<Integer>();
		for (String tmp : strs) {
			Integer number = new Integer(tmp);
			intList.add(number);
		}
		return intList;
	}
	public void GenerateTable() {
		if (instanceList == null) {
			return;
		}
		System.out.println("\t"+"result"+ " " + "Time(s)");
		
		for (Instance ins : instanceList) {
			System.out.print(ins.getId() + "   ");
			System.out.print(ins.getResult());
			System.out.print("   ");
			// 转化为毫秒
			System.out.print((double)ins.getTime()/1000);
			System.out.print("\n");
		}
	}
	public void DisplayInstance() {
		if (instanceList == null) {
			return;
		}
		for (Instance ins : instanceList) {
			System.out.println(ins.getResult());
			for (Boolean bool : ins.getOpenList()) {
				System.out.print(bool ? 1 : 0);
				System.out.print(" ");
			}
			System.out.println("");
			for (Integer tmp : ins.getAssignmentList()) {
				System.out.print(tmp.intValue());
				System.out.print(" ");
			}
			System.out.println("");
		}
	}
	public Instance GenerateInstance(String id) {
		Instance ins = new Instance();
		long t1 = System.currentTimeMillis();
		//int result = Greedy();
		int result = SimulateAnneal();
		long t2 = System.currentTimeMillis();
		List<Boolean> openList = new ArrayList<Boolean>();
		List<Integer> assignmentList = new ArrayList<Integer>();
		for (Facility faci : facilityList) {
			openList.add(faci.isOpen());
		}
		for (Customer cust : customerList) {
			assignmentList.add(cust.getAssignedTo());
		}
		ins.setId(id);
		ins.setResult(result);
		ins.setTime((int)(t2-t1));
		ins.setOpenList(openList);
		ins.setAssignmentList(assignmentList);
		
		return ins;
	}
	public int Greedy() {
		int result = 0;
		for (Customer cust : customerList) {
			int demand = cust.getDemand();
			int cost = Integer.MAX_VALUE;
			int faciId = -1;
			for (Facility faci : facilityList) {
				Map<Integer, Integer> assignmentMap = faci.getAssignmentCost();
				int assignmentCost = assignmentMap.get(cust.getCustomerId());
				if (assignmentCost < cost && faci.getCapacity() >= demand) {
					cost = assignmentCost;
					faciId = faci.getFacilityId();
				}	
			}
			cust.setAssignedTo(faciId);
			if (faciId >= 0) {
				Facility faci = facilityList.get(faciId);
				result += cost;
				if (!faci.isOpen()) {
					faci.setOpen(true);
					result += faci.getCost();
				}
				faci.setCapacity(faci.getCapacity()-demand);
			}

		}
		return result;
	}
	public int SimulateAnneal() {
		double temper = 100000; //初始温度
		double minTemper = 0.001; //终止温度
		double coolRate = 0.99;
		double count = 1000;
		// 初始状态，为了方便选用贪婪算法的解
		int bestVal = Greedy();
		int curVal = bestVal;
		int nextVal = bestVal;
		List<Facility> facilityListBestCopy = new ArrayList<Facility>();
		List<Customer> customerListBestCopy = new ArrayList<Customer>();
		for (Facility faci : facilityList) {
			facilityListBestCopy.add(new Facility(faci));
		}
		for (Customer cust : customerList) {
			customerListBestCopy.add(new Customer(cust));
		}
		while (temper > minTemper) {
			for (int i = 0; i < count; i++) {
				//拷贝，用于还原
				List<Facility> facilityListCopy = new ArrayList<Facility>();
				List<Customer> customerListCopy = new ArrayList<Customer>();
				for (Facility faci : facilityList) {
					facilityListCopy.add(new Facility(faci));
				}
				for (Customer cust : customerList) {
					customerListCopy.add(new Customer(cust));
				}
				nextVal = GetNextResult(curVal);
				double delta = nextVal - curVal;
				if (delta < 0) {
					curVal = nextVal;
				} else {
					if (Math.exp(-delta/temper) > Math.random()) {
						curVal = nextVal;
					} else {
						facilityList = facilityListCopy;
						customerList = customerListCopy;
					}
				}
			}
			
			if (curVal < bestVal) {
				bestVal = curVal;
				facilityListBestCopy = facilityList;
				customerListBestCopy = customerList;
			}
			temper *= coolRate;
		}
		facilityList = facilityListBestCopy;
		customerList = customerListBestCopy;
		return bestVal;
	}
	//交换
	public int GetNextResult(int lastResult) {
		Random rand = new Random();
		int Val = rand.nextInt(2);
		int newResult = lastResult;
		switch(Val) {
			// 交换两个顾客
			case 0:
				Customer cust1 = customerList.get(rand.nextInt(customerList.size()));
				Customer cust2 = customerList.get(rand.nextInt(customerList.size()));
				//检查是否再同一设施，相同则跳出
				if (cust1.getAssignedTo() == cust2.getAssignedTo())
					break;
				//检查交换是否足够空间
				Facility faci1 = facilityList.get(cust1.getAssignedTo());
				Facility faci2 = facilityList.get(cust2.getAssignedTo());
				if (faci1.getCapacity()+cust1.getDemand() < cust2.getDemand())
					break;
				if (faci2.getCapacity()+cust2.getDemand() < cust1.getDemand())
					break;
				//开始交换
				cust1.setAssignedTo(faci2.getFacilityId());
				cust2.setAssignedTo(faci1.getFacilityId());
				faci1.setCapacity(faci1.getCapacity()+cust1.getDemand()-cust2.getDemand());
				faci2.setCapacity(faci2.getCapacity()+cust2.getDemand()-cust1.getDemand());
				newResult += faci1.getAssignmentCost().get(cust2.getCustomerId())
						-faci1.getAssignmentCost().get(cust1.getCustomerId());
				newResult += faci2.getAssignmentCost().get(cust1.getCustomerId())
						-faci2.getAssignmentCost().get(cust2.getCustomerId());
				break;
			// 指定一个顾客到另一个设施(可以是原来那个)
			case 1:
				Customer cust = customerList.get(rand.nextInt(customerList.size()));
				Facility faci = facilityList.get(rand.nextInt(facilityList.size()));
				Facility oldFaci = facilityList.get(cust.getAssignedTo());
				//检查是否同一设施
				if (cust.getAssignedTo() == faci.getFacilityId())
					break;
				//检查是否足够空间
				if (faci.getCapacity() < cust.getDemand())
					break;
				//开始交换
				cust.setAssignedTo(faci.getFacilityId());
				newResult += faci.isOpen() ? 0 : faci.getCost();
				faci.setOpen(true);
				faci.setCapacity(faci.getCapacity()-cust.getDemand());
				newResult += faci.getAssignmentCost().get(cust.getCustomerId())
						-oldFaci.getAssignmentCost().get(cust.getCustomerId());
				int pointer = 0; //多少个指向旧设施
				for (Customer tmp: customerList) {
					if (tmp.getAssignedTo() == oldFaci.getFacilityId()) {
						pointer++;
					}
				}
				// 没有则关闭设施
				if (pointer == 0) {
					oldFaci.setOpen(false);
					newResult -= oldFaci.getCost();
				}
				oldFaci.setCapacity(oldFaci.getCapacity()+cust.getDemand());
				
				break;
		}
		//System.out.println(newResult-lastResult);
		return newResult;
	}
	public void GenerateAllInstances() {
		// 初始化List
		facilityList = new ArrayList<Facility>();
		customerList = new ArrayList<Customer>();
		instanceList = new ArrayList<Instance>();
		for (Integer i = 1; i <= INSTANCENUM; i++) {
			facilityList.clear();
			customerList.clear();
			ReadFileAndInit(Path+"\\" + "p"+ i.toString());
			Instance ins = GenerateInstance("p"+ i.toString());
			instanceList.add(ins);
		}
	}
	public static void main(String[] args) {
		Solution sa = new Solution();
		sa.GenerateAllInstances();
		sa.DisplayInstance();
		sa.GenerateTable();
	}

}
