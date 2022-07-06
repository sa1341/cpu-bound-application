package class101.foo.cpu.fund;

import class101.foo.cpu.fund.dto.FndProdDto;
import class101.foo.cpu.fund.service.FundRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RestController
public class FundApi {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final FundRestService fundRestService;

    public FundApi(FundRestService fundRestService) {
        this.fundRestService = fundRestService;
    }

    @PostMapping("/fund")
    public ResponseEntity<String> syncFundInfo(@RequestBody final FndProdDto fndProdDto) throws Exception {

        List<FndProdDto> fndProducts = new ArrayList<>();
        fndProducts.add(new FndProdDto("2022567"));
        fndProducts.add(new FndProdDto("1044167"));
        fndProducts.add(new FndProdDto("3322167"));
        fndProducts.add(new FndProdDto("7333177"));


        fndProducts.forEach(fnd -> {
            try {
                fundRestService.sendFundInfo(fnd);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
