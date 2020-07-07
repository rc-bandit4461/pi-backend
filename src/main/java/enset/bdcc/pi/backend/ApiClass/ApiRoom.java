package enset.bdcc.pi.backend.ApiClass;

import lombok.Data;

@Data
public class ApiRoom {
    public int id;
    public String name;
    public String location;
    public int seats;
    public Boolean state;
    public String detail;
    public int pcs;
    public int projector;
    public int board;
}
