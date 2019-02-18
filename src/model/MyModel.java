package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyModel {
    private Connection connection;

    public MyModel() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/mainacaddemo1",
                    "eugeny", "123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> findAllPeople() {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from person")
        ) {
            List<Person> result = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                double salary = rs.getDouble("salary");
                result.add(new Person(id,name,age,salary));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void createPerson(String name, int age, double salary) {
        try (PreparedStatement ps = connection.prepareStatement("insert into person (name, age, salary) values (?,?,?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setDouble(3, salary);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(Person person) {
        try (PreparedStatement ps = connection.prepareStatement("delete from person where id = ?")) {
            ps.setInt(1, person.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editPerson(Person person) {
        try (PreparedStatement ps = connection.prepareStatement("update person set name = ?, age = ?, salary = ? where id = ?")) {
            ps.setInt(4, person.getId());
            ps.setString(1, person.getName());
            ps.setInt(2, person.getAge());
            ps.setDouble(3, person.getSalary());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
