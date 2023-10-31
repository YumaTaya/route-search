package routesearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import routesearch.data.Station;
import routesearch.data.StationBranch;


public class Dist {
    public static List<Node> open_list = new ArrayList<>();
    public static List<Node> close_list = new ArrayList<>();

    public static float distance(Station start, Station goal) {
        Node min_node = null;
        // float min_distance = 0;
        boolean searching = true;

        open_list.clear();
        close_list.clear();

        open_list.add(new Node(start, 0, 0));

        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node nodeA, Node nodeB) {
                return Float.valueOf(nodeA.getF()).compareTo(Float.valueOf(nodeB.getF()));
            }
        };

        while (searching) {
            min_node = Collections.min(open_list, comparator);

            if (min_node.getS().getName().equals(goal.getName())) {
                searching = false;
            } else {
                min_node.open(goal);
            }
        }

        return (float) (Math.round(min_node.getG() * 10) / 10);
    }

    public static float heuristic(Station st_a, Station st_b) {
        //double POLE_RADIUS = 6356752;    // 極半径(短半径) Ry
        double EQUATOR_RADIUS = 6378137; // 赤道半径(長半径) Rx
        //double E = 0.081819191042815790; // 離心率
        double E2= 0.006694380022900788; // 離心率の２乗

        double lat_radius_a = Math.toRadians(st_a.getY());
        double lon_radius_a = Math.toRadians(st_a.getX());
        double lat_radius_b = Math.toRadians(st_b.getY());
        double lon_radius_b = Math.toRadians(st_b.getX());

        double dy = (lat_radius_a - lat_radius_b);
        double dx = (lon_radius_a - lon_radius_b);
        double p = (lat_radius_a + lat_radius_b) / 2;

        double w = Math.sqrt(1-E2*Math.pow(Math.sin(p), 2));
        double m = EQUATOR_RADIUS*(1-E2) / Math.pow(w, 3); // 子午線曲率半径
        double n = EQUATOR_RADIUS / w; //卯酉線曲率半径
        double d = Math.sqrt(Math.pow(dy*m, 2) + Math.pow(dx*n*Math.cos(p), 2));
      
        return (float) (Math.round(d/100) / 10);
    }

    static class Node {
        private Station s;
        private float f;
        private float g;

        public Node(Station s, float f, float g) {
            this.s = s;
            this.f = f;
            this.g = g;
        }

        public void open(Station goal) {
            for (StationBranch b : this.s.getBranch()) {
                float new_g = this.g + b.getWeight(); // dをgetWeight()に書き換えた
                Station next_st = b.nextStation(this.s);

                new Node(next_st, new_g + heuristic(next_st, goal), new_g).updateList();
            }
            open_list.remove(this);
            close_list.add(this);
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof Node) {
                Node n = (Node) obj;
                // return this.s.getName().equals(n.s.getName()); 何故かできそうな雰囲気がある
                return this.s.getName().equals(n.getS().getName());
            }
            /*
             * else if(obj instanceof Station) {
             * Station st = (Station) obj;
             * return this.s.getName().equals(st.getName());
             * }
             */
            return false;
        }
        // 通常、equals関数をオーバーライドする場合は、hashCode関数もオーバーライドする必要があるが、
        // 今回のコードでは、hashCodeが関係するコレクション等を特に使用していないため、省略する。

        public void updateList() {
            for (Node op : open_list) {
                if (this.equals(op)) {
                    if (this.f < op.getF()) {
                        open_list.remove(op);
                        break;
                    } else
                        return;
                }
            }

            for (Node cl : close_list) {
                if (this.equals(cl)) {
                    if (this.f < cl.getF()) {
                        close_list.remove(cl);
                        break;
                    } else
                        return;
                }
            }

            open_list.add(this);
        }

        public float getF() {
            return this.f;
        }

        public Station getS() {
            return this.s;
        }

        public float getG() {
            return this.g;
        }
    }
}
/*
//下のStationクラス、Station_branchクラスは実際には他のモジュールからインポートしているはずなので、あしからず。
class Station {
    private String name;
    private List<StationBranch> branch;
    private float x, y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StationBranch> getBranch() {
        return branch;
    }

    public void setBranch(List<StationBranch> branch) {
        this.branch = branch;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

class Station_branch {
    Station stationA, stationB;
    float d;

    public Station next_station(Station s) {
        if (this.stationA.equals(s))
            return stationB;
        else if (this.stationB.equals(s))
            return stationA;
        else
            return null;
    }
}
*/
