package enset.bdcc.pi.backend.ApiClass;

import lombok.Data;

import java.sql.Date;

@Data
public class ApiBooking {
    private Long id;
    private Date date;
    private String start;
    private String end;
    private String title;
    private Long roomId;
    private Long userId;
}
