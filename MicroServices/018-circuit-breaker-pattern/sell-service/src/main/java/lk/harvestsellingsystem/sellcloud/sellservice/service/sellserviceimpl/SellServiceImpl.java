package lk.harvestsellingsystem.sellcloud.sellservice.service.sellserviceimpl;

import lk.harvestsellingsystem.sellcloud.sellservice.hystricx.CommonHystrixCommand;
import lk.harvestsellingsystem.sellcloud.sellservice.hystricx.ItemCommand;
import lk.harvestsellingsystem.sellcloud.sellservice.model.modelimpl.CreateSell;
import lk.harvestsellingsystem.sellcloud.sellservice.model.modelimpl.DetailResponse;
import lk.harvestsellingsystem.sellcloud.sellservice.model.Response;
import lk.harvestsellingsystem.sellcloud.sellservice.model.modelimpl.HalfResponse;
import lk.harvestsellingsystem.sellcloud.sellservice.repository.SellRepository;
import lk.harvestsellingsystem.sellcloud.sellservice.service.SellService;
import lk.harvestsellingsystem.sellcloud.servicecloudmodel.model.item.Item;
import lk.harvestsellingsystem.sellcloud.servicecloudmodel.model.sell.Sell;
import lk.harvestsellingsystem.sellcloud.servicecloudmodel.model.user.Buyer;
import lk.harvestsellingsystem.sellcloud.servicecloudmodel.model.user.Farmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class SellServiceImpl implements SellService {

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder){

        return builder.build();
    }

    @Autowired
    SellRepository sellRepository;


    @Autowired
    RestTemplate restTemplate;

    @Override
    public Sell save(CreateSell createSell, int farmerId) {
        Item item = new Item(createSell.getHarvestType(), createSell.getCategory(), createSell.getPricePerUnit(), createSell.getPriceUnit(), createSell.getQuantity(), farmerId);
         item = (Item)saveItem(item,farmerId).getBody();
        System.out.println(item.getCategory()+" "+item.getHarvestType());
        Sell sell = new Sell();
        sell.setFarmerId(farmerId);
        sell.setItemId(item.getId());

        return sellRepository.save(sell);
    }

    @Override
    public Sell findById(int id) {
        Optional<Sell> sell = sellRepository.findById(id);
        if(sell.isPresent()) {
            return sell.get();
        } else {
            return new Sell();
        }
    }

    @Override
    public HalfResponse findHalfDetails(int id) {
        Sell sell = findById(id);
        Item item = getItem(sell.getItemId());
        return new HalfResponse(sell,item);
    }


    @Override
    public DetailResponse findDetails(int id) throws ExecutionException, InterruptedException {
        Sell sell = findById(id);
        Item item = getItem(sell.getItemId());
        Farmer farmer = getFarmer(sell.getFarmerId());
        Buyer buyer = getBuyer(sell.getBuyerId());
        return new DetailResponse(sell,farmer,buyer,item);
    }

    private Farmer getFarmer(int farmerId) throws ExecutionException, InterruptedException {

        CommonHystrixCommand<Farmer> farmerCommonHystrixCommand = new CommonHystrixCommand<Farmer>("default",()->{
            return restTemplate.getForObject("http://user/services/farmer/"+farmerId,Farmer.class);
        },()->{
            return null;
        });

        Future<Farmer> farmerFuture = farmerCommonHystrixCommand.queue();
        return farmerFuture.get();

        //return restTemplate.getForObject("http://user/services/farmer/"+farmerId,Farmer.class);
    }

    private Buyer getBuyer(int buyerId) throws ExecutionException, InterruptedException {
        CommonHystrixCommand<Buyer> buyerCommonHystrixCommand = new CommonHystrixCommand<Buyer>("default",()->{
            return restTemplate.getForObject("http://user/services/buyer/" + buyerId, Buyer.class);
        },()->{
            return null;
        });

        Future<Buyer> buyerFuture = buyerCommonHystrixCommand.queue();
        return buyerFuture.get();

//        if(buyerId > 0) {
//            return restTemplate.getForObject("http://user/services/buyer/" + buyerId, Buyer.class);
//        } else {
//            return null;
//        }
    }

    private Item getItem(int itemId) {
        //return restTemplate.getForObject("http://item/services/item/"+itemId,Item.class);
        ItemCommand itemCommand =new ItemCommand(restTemplate,itemId);
        return itemCommand.execute();
    }

    private ResponseEntity<Item> saveItem(Item item, int farmerId) {
        return restTemplate.postForEntity("http://item/services/item/"+farmerId, item, Item.class);
    }
}
