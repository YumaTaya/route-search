package routesearch;
import java.util.ArrayList;
import java.util.Calendar;

import routesearch.data.Data;
import routesearch.data.Line;
import routesearch.data.Station;



public class Transfer {
	public TransferResult[] minTimeResult(Station start, Station goal, Calendar date, int searchNum, boolean isFrom, Data data){
		searchNum = 1; // 現時点では候補1つに固定
		TransferResult[] result = new TransferResult[searchNum];
		int time = calendarToTime(date);
		date = reCalendar(date);
		ArrayList<Line> lineInterList = new ArrayList<>(start.getLineList()); 
		lineInterList.retainAll(goal.getLineList());
		
		ArrayList<TransferResult> choiceList = new ArrayList<>();//最短時間経路の候補が入るArrayList
		
		if(lineInterList.size()==0) {//乗換しないといけない場合(乗換するときの経路も探索できるようになれば、乗り換えしないでいける経路探索と分岐させるは必要ない)
			System.out.println("乗換する");
			return result; //現時点ではnullのみの配列を返す(経路がみつからないと出力される)
		}
		
		//ここのforは時刻表の扱いを確認するために仮で作っているが、乗換を考慮した探索ができればfor文内すべて飛ばす(消す)
		for(int i=0; i<lineInterList.size(); i++) {
			System.out.println("乗換しなくても行ける");
			Line line = lineInterList.get(i);
			if(line.getIsLoop()) {
				System.out.println("環状線");
				
				return result;
			}else {
				System.out.println("環状線じゃない");
				Station[] sToGSta = line.getStationList();
				ArrayList<Station> sToGStaList = new ArrayList<>();
				//System.out.println(sToGSta.length);
				for(int j = 0 ;j<sToGSta.length;j++) {//listをArrayListに変換
					sToGStaList.add(sToGSta[j]);//
				}
				for(int j=0; j<sToGStaList.size(); j++) {
					if(start.equals( sToGStaList.get(0)) ) {
						break;
					}
					sToGStaList.remove(0);
				}

				if(sToGStaList.contains(goal)) {
					TransferResult r = oneLineSearch(line,start,goal,date,time);
					if(r!=null) {
						if(choiceList.size()>0) {
							//出発時間が早い方が小さいインデックスになるようにソートしながら候補に追加
							boolean isLast = true;
							for(int k =0; k<choiceList.size(); k++) {
								if(choiceList.get(k).getGDate().after(r.getGDate())) {
									choiceList.add(k,r);
									isLast = false;
									break;
								}
							}
							if(isLast) {
								choiceList.add(choiceList.size(),r);
							}
						}else {//まだ1つ目の候補のとき
							choiceList.add(r);
						}
					}
				}
			}
		}
		
		if(choiceList.size()==0) {
			System.out.println("経路が1つも見つからない");
			return result; //経路が1つも見つからない
		}
		
		//作成中 result = getSortTransfer(choiceList,result.length).clone();
		/*
		ArrayList<Station> stList = new ArrayList<>(Arrays.asList(start, goal));
		Line line1 = new Line("路線1",stList,false,"");
		
		TransferResult r1 = new TransferResult(start,goal,date,95500,101100,true,line1,goal,-1,-1,-1,false,false);

		resList[0] = r1; //最初のTransferResult
		*/
		for(int i = 0; i<result.length && i<choiceList.size(); i++) {
			result[i]=choiceList.get(i);
			System.out.println("result["+i+"]: gDate="+result[i].getGDate().getTime());
		}
		if(result[0]!=null) {
			System.out.println("result is not null");
		}
		return result;
	}
	
	private int calendarToTime(Calendar date) {
		int hour = date.get(Calendar.HOUR);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);
		int time = hour*10000 + minute*100 + second;
		if(time<30000) {
			time = time + 240000;
		}
		return time;
	}
	
	private Calendar reCalendar(Calendar date) {
		if(date.get(Calendar.HOUR_OF_DAY)<3) {
			date.add(Calendar.DAY_OF_MONTH, -1);
		}
		return date;
	}
	
	private TransferResult oneLineSearch(Line line, Station start, Station goal, Calendar date, int time) {
		System.out.println("oneLineSearch");
		if(!(line.isComplete())) {
			System.out.println(line.getName()+"は登録データが不足しています");
			return null;
		}
		
		int dn = line.diaNum(date);
		int i,j;
		for(i = 0; i < line.getDiagram().get(dn*2).length-2; i++) {
			if(line.getDiagram().get(dn*2)[i][0].equals(start.getName())) {
				System.out.println(start.getName()+"は"+(i+1)+"行目");
				break;
			}
		}
		
		for(j = 0; j < line.getDiagram().get(dn*2+1).length-2; j++) {
			if(line.getDiagram().get(dn*2+1)[j][0].equals(goal.getName())) {
				System.out.println(goal.getName()+"は"+(j+1)+"行目");
				break;
			}
		}
		
		TransferResult result = search(start,goal,line,date,time,dn,i,j,false);
		if(result==null) {
			//Calendar nextDate = date;
			//nextDate.add(Calendar.DAY_OF_MONTH, 1);
			//dn = line.diaNum(nextDate);
			date.add(Calendar.DAY_OF_MONTH, 1);
			dn = line.diaNum(date);
			result = search(start,goal,line,date,30000,dn,i,j,false);
		}
		
		if(result==null) {
			System.out.println("Exception: oneLineSearch");
		}
		
		System.out.println("oneLineSearch: gDate="+result.getGDate().getTime());
		return result;
	}
	
	private TransferResult search(Station start, Station goal, Line line, Calendar date, int time, int dn, int i1, int i2, boolean isLoop) {
		//!isLoopのとき
		for(int j=1; j<line.getDiagram().get(dn*2)[i1].length; j++) {
			String sTimeStr = String.valueOf(line.getDiagram().get(dn*2)[i1][j]);
			String gTimeStr = String.valueOf(line.getDiagram().get(dn*2+1)[i2][j]);
			if(sTimeStr.matches("[0-9]+") && gTimeStr.matches("[0-9]+")) {
	
				int startTime = Integer.parseInt(sTimeStr);
				int goalTime = Integer.parseInt(gTimeStr);
				if(startTime>=time) {
					String terminalName = String.valueOf(line.getDiagram().get(dn*2+1)[line.getDiagram().get(dn*2+1).length-2][j]);
					Station terminal = parseStation(line,terminalName);
					if(terminal==null) {
						System.out.println("search exception");
						continue;
					}
					TransferResult result = new TransferResult(start,goal,date,startTime,goalTime,true,line,terminal,-1,-1,-1,false,false);
					return result;
				}
			}else {
				continue;
			}	
		}
		return null;
	}
	
	private Station parseStation(Line line, String stationName) {
		for(Station station: line.getStationList()) {
			if(station.getName().equals(stationName)) {
				return station;
			}
		}
		return null;
	}
	
	private TransferResult[] getSortTransfer(ArrayList<TransferResult> choiceList, int resLen) {
		TransferResult[] result = new TransferResult[resLen];
		//作成中
		return result;
	}
	/*
	public ArrayList<ArrayList<TransferResult>> cloneArrArr(ArrayList<ArrayList<TransferResult>> ListList){
		ArrayList<ArrayList<TransferResult>> resListList = new ArrayList<>();
		for(int i=0; i<ListList.size(); i++) {
			resListList.addAll(ListList.get(i).clone());
		}
		return resListList;
	}
	*/
}
/*
public class TransferResultComparator implements Comparator<TransferResult>{
	
	@Override
	public Calendar
}
*/