package class101.foo.cpu.fund.dto;


public class FndProdDto {
    private String fundCod;

    public FndProdDto() {
    }

    public FndProdDto(String fundCod) {
        this.fundCod = fundCod;
    }

    public String getFundCod() {
        return fundCod;
    }

    @Override
    public String toString() {
        return "FndProdDto{" +
                "fundCod='" + fundCod + '\'' +
                '}';
    }
}
