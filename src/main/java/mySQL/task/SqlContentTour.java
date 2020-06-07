package mySQL.task;

import mySQL.ConnectSQL.CreatConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlContentTour {
    //tra ve content_tour_id
    public int search_cttID(int tour_id,int content_id){
        int result=-1;
        CreatConnector con = new CreatConnector();
        Connection connection = con.getConnect();
        try {

                String sql = "SELECT content_tour_id\n" +
                        "FROM content_tour\n" +
                        "WHERE content_tour.content_id=" + content_id + "  AND content_tour.tour_id=" + tour_id;
                // tien hanh tim content_tour_id tuong ung voi tour_id va content_id
                PreparedStatement ps=connection.prepareStatement(sql);
                ResultSet rs= ps.executeQuery();
                if(rs.next()) result= rs.getInt(1);// tra ve content_tour_id mong muong
                else result= 0;// khong tim thay content_tour_id
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                System.out.println("disconneted");
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        return result;
    }

}
