package routesearch.data;
import java.util.ArrayList;

/* 駅情報 */
public class Station{
  private String name; //駅名
  private ArrayList<Station> nextSt = new ArrayList<Station>(); //隣り合った駅
  private ArrayList<StationBranch> branch = new ArrayList<StationBranch>(); //接続される区間(グラフ理論でいうところの辺)
  private ArrayList<Integer> numList = new ArrayList<Integer>(); //駅の保有する番線 [1,2,3,4]
  private float x = -1.0f; //地図上の座標x
  private float y = -1.0f; //座標y
  private int transferMin = -1; //乗り換えに要する時間
  private ArrayList<Line> lineList = new ArrayList<Line>(); //駅の属する路線


  public Station(String name){
    this.name = name;
  }

  // 最低限持っていなければならない駅情報の確認
  public boolean isComplete(){ 
    return nextSt.size() >= 1 &&
            branch.size() >= 1 &&
            numList.size() >= 1 &&
            x >= 0 &&
            y >= 0 &&
            transferMin >= 0;
  }

  
  //add
  public void addLine(Line line) {// 10/27追記(他谷)
	  lineList.add(line);
  }
  
  /* getter setter */

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setTransferMin(int transferMin) {
    this.transferMin = transferMin;
  }

  public String getName(){
    return name;
  }

  public ArrayList<Station> getNextSt() {
    return nextSt;
  }

  public ArrayList<StationBranch> getBranch() {
    return branch;
  }

  public ArrayList<Integer> getNumList() {
    return numList;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public int getTransferMin() {
    return transferMin;
  }

  public ArrayList<Line> getLineList() {
    return lineList;
  }

}