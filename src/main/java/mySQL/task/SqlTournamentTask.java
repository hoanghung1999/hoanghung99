package mySQL.task;

import mySQL.ConnectSQL.CreatConnector;
import oop.Tournament;
import org.json.simple.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SqlTournamentTask {
        // tra ve key cua tournament vua cai dat
        public int insertTour(Tournament tournament){
                int result=-1;
        String sql="INSERT IGNORE INTO tournament(tour_name,tour_start_date,tour_end_date,tour_location_date,tour_description)" +
                "Values(?,?,?,?,?)";
        CreatConnector con=new CreatConnector();
        Connection connection= con.getConnect();
                try {
                        PreparedStatement preparedStatement=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setString(1,tournament.getTour_name());
                        preparedStatement.setDate(2, (Date) tournament.getTour_start_date());
                        preparedStatement.setDate(3, (Date) tournament.getTour_end_date());

                        preparedStatement.setString(4,tournament.getTour_location_date());
                        preparedStatement.setString(5,tournament.getTour_description());
                        preparedStatement.execute();
                        ResultSet rs= preparedStatement.getGeneratedKeys();

                        if(rs.next()){
                                result= rs.getInt(1);// key cua tournament vua tao
                        }
                        else result= 0;// tournament  vua tao khong thanh cong
                        preparedStatement.close();
                        rs.close();
                } catch (SQLException e) {
                        e.printStackTrace();
                }finally {
                        try {
                                connection.close();
                                System.out.println("disconneted");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                return result;// tra ve PK cua tournament duoc cai dat, neu <=0 thì them tour ko thanh cong
        }

        //kết quả trả về khác 0 là update thành công
        public int updateTour(Tournament tournament){
                int result=0;
                String sql="Update tournament SET " +
                        "tour_name = "+"'"+tournament.getTour_name()+"',"+
                        "tour_start_date ="+"'"+tournament.getTour_start_date()+"',"+
                        "tour_end_date = "+"'"+tournament.getTour_end_date()+"',"+
                        "tour_location_date ="+"'"+tournament.getTour_location_date()+"',"+
                        "tour_description ="+"'"+tournament.getTour_description()+"' "+
                        "where tour_id ="+tournament.getTour_id();

                CreatConnector con=new CreatConnector();
                Connection connection=con.getConnect();
                try {
                        PreparedStatement ps= connection.prepareStatement(sql);

                       result=  ps.executeUpdate();
                       ps.close();
//                        trả về số dòng bị thay đổi trong DB
                } catch (SQLException e) {
                        e.printStackTrace();
                        result=-1;

                }finally {
                        try {
                                connection.close();
                                System.out.println("disconnected");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                return result;
//                lỗi kết nối JDBC
        };
        //kq trả về lớn hơn 0 là thành công
        public int deleteTourByID(int id){
                int result=0;
                String sql="UPDATE tournament SET is_delete=1 WHERE tour_id="+id;
                CreatConnector con= new CreatConnector();
                Connection connection= con.getConnect();
                try {
                        PreparedStatement ps= connection.prepareStatement(sql);
                        result= ps.executeUpdate();
                        ps.close();
                } catch (SQLException e) {
                        e.printStackTrace();
                        result=-1;
                }finally {
                        try {
                                connection.close();
                                System.out.println("disconnected");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                return result;
        }

        public Tournament getTourByID(int id){
                String sql="SELECT * FROM tournament WHERE tour_id="+id;
                CreatConnector con= new CreatConnector();
                Connection connection= con.getConnect();
                Tournament tournament=null;
                try {
                        PreparedStatement ps= connection.prepareStatement(sql);
                        ResultSet rs=ps.executeQuery();
                        while (rs.next()){
                                tournament = new Tournament(rs.getInt(1),rs.getString(2),
                                        rs.getDate(3),rs.getDate(4),
                                        rs.getString(5),rs.getString(6));

                        }
                       ps.close();
                        rs.close();
                } catch (SQLException e) {
                        e.printStackTrace();
                }finally {
                        try {
                                connection.close();
                                System.out.println("disconnected");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                return tournament;
        }

        public List<JSONObject> getTourbyName(String name){
                String sql="SELECT * FROM tournament " +
                        "WHERE tour_name like '%"+name+"%'";
                List<JSONObject> list= new ArrayList<>();
                CreatConnector con= new CreatConnector();
                Connection connection= con.getConnect();
                try {
                        PreparedStatement ps= connection.prepareStatement(sql);
                        ResultSet rs= ps.executeQuery();
                        while (rs.next()){
                                Tournament tournament= new Tournament(
                                        rs.getInt(1),rs.getString(2),rs.getDate(3),
                                        rs.getDate(4),rs.getString(5),rs.getString(6)
                                );
                                list.add(tournament.toJson());

                        }
                        ps.close();
                        rs.close();
                } catch (SQLException e) {
                        e.printStackTrace();
                }finally {
                        try {
                                connection.close();
                                System.out.println("disconnected");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                        return list;
                }
        }

        public  List<JSONObject> getAllTour(){
                String sql="SELECT * \n" +
                        "FROM tournament\n" +
                        "ORDER BY status, tour_start_date";
                List<JSONObject> list_tour=new ArrayList<>();
                CreatConnector con= new CreatConnector();
                Connection connection=con.getConnect();
                try {
                       PreparedStatement ps=connection.prepareStatement(sql);
                       ResultSet rs=ps.executeQuery();
                       while (rs.next()){
                               Tournament tour=new Tournament(rs.getInt(1),rs.getString(2),
                                       rs.getDate(3),rs.getDate(4),rs.getString(5)
                               ,rs.getString(6),rs.getInt(7));
                       list_tour.add(tour.toJson());
                       }
                       ps.close();
                       rs.close();
                }catch (Exception e){
                        e.printStackTrace();
                }finally {
                        try {
                                connection.close();
                                System.out.println("disconnected");
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                return list_tour;
        }

}
