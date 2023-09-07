package nation;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Quali paesi?");
        String search = scan.nextLine();
        System.out.println("Cerco i paesi che contengono " + search);

        // variabili in cui salvo i parametri di connessione al db
        String url = "jdbc:mysql://localhost:3306/db-nations";
        String user = "root";
        String password = "julie";

        // preparo la variabile che contiene la connessione



Connection connection = null;
/*try {
      // provo a farmi dare la connessione dal driver manager
      connection = DriverManager.getConnection(url, user, password);
      // stampo a video il nome dello schema
      System.out.println(connection.getCatalog());
    } catch (SQLException e) {
      System.out.println("Unable to connect to database");
    } finally {
      // in ogni caso passo da qui e se la connessione era aperta provo a chiuderla
      if(connection != null){
        try {
          connection.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    }*/
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            // ho a disposizione la risorsa Connectio conn che ho aperto
            //System.out.println(conn.getCatalog());
            // uso la connessione per preparare uno statement sql
            // NO RISCHIO SQL INJECTION !!!!!
            // String sql = "select * from departments where name like '%" + search + "%' order by name;";
            String sql = "select countries.name as nation ,countries.country_id as nation_id ,regions.name as region,continents.name as continent from countries join regions  on  countries.region_id = regions.region_id join continents on regions.continent_id =continents.continent_id where countries.name like ? order by countries.name; " ;

            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                // prima di eseguire il prepared statement faccio il binding dei parametri

                ps.setString(1,  "%"+search+"%");// il primo parametro (1) vale quello che c'è nella variabile search


                // eseguo la query e la inserisco in un oggetto ResultSet
                try (ResultSet rs = ps.executeQuery()) {
                    // posso iterare sul result set
                    while (rs.next()) {
                       String nation = rs.getString("nation");
                      int  id = rs.getInt("nation_id");
                        String region = rs.getString("region");
                       String continent = rs.getString("continent");
                        System.out.println(nation + "  " + id + " " + region + " " + continent);
                    }
                }
            }

        } catch(SQLException e){
            System.out.println("OOPS an error occurred");
            e.printStackTrace();
        }

    }
}
