package enset.bdcc.pi.backend.ApiClass;

import lombok.Data;

import java.sql.Date;

@Data
public class ApiBooking {
    private int id;
    private Date date;
    private String start;
    private String end;
    private String title;
    private int roomId;
    private Long userId;
}
