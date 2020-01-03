package com.jk.controller;

import com.jk.model.CarModel;
import com.jk.service.CarService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private SolrClient client;

    //查询+分页
    @GetMapping("queryCar")
    public Map<String, Object> queryPostsList(Integer page, Integer rows, CarModel carModel) {

        //返回到前台
        Map<String, Object> map1=new HashMap<>();

        StringBuilder str = new StringBuilder();
        try {
            //存放所有的查询条件
            SolrQuery params = new SolrQuery();

//            查询条件, 这里的 q 对应 下面图片标红的地方
            if (carModel.getCarName()!=null && !"".equals(carModel.getCarName())){     //第一个条件无法确定是否会有值，所以需要判断
                str.append("carName: "+carModel.getCarName());
            }else {
                str.append("carName:*");   //如果title为null，这个条件是查询所有，这样后面的AND就不会报错
            }

            if(carModel.getCarprice()!=null && !"".equals(carModel.getCarprice())){
                str.append("and carprice: "+carModel.getCarprice());
            }

            //过滤条件
            // params.set("fq", "carPrice:["+car.get+" TO "++"]");

            //排序
            //params.addSort("poststime", SolrQuery.ORDER.desc);

            params.setQuery(str.toString());
            //分页
            if(page==null){
                params.setStart(0);
            }else {
                params.setStart((page-1)*rows);
            }
            if(rows==null){
                params.setRows(5);
            }else {

                params.setRows(rows);
            }
            //默认域
            params.set("df", "product_keywords");

            //只查询指定域
            //params.set("fl", "id,product_title,product_price");
            //高亮
            //打开开关
            params.setHighlight(false);
            //指定高亮域
            params.addHighlightField("carName");
            //设置前缀
            params.setHighlightSimplePre("<span style='color:red'>");
            //设置后缀
            params.setHighlightSimplePost("</span>");

            //查询后返回的对象
            QueryResponse queryResponse = client.query("core1",params);
            //查询后返回的对象 获得真正的查询结果
            SolrDocumentList results = queryResponse.getResults();
            //查询的总条数
            long numFound = results.getNumFound();

            //System.out.println(numFound);

            //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
            Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();

            //创建list集合接收我们循环的参数
            List<CarModel> list1 =new ArrayList<>();
            for (SolrDocument result : results) {

                CarModel car =new CarModel();

                String highFile= "";

                Map<String, List<String>> map = highlight.get(result.get("id"));

                List<String> list = map.get("carName");
                if(list==null){
                    highFile = (String) result.get("carName");
                }else {
                    highFile=list.get(0);
                }

                car.setCarId(Integer.parseInt(result.get("id").toString()));
                car.setCarImg((String)result.get("carImg"));
                car.setCarName((String)result.get("carName"));
                car.setCarprice((Double) result.get("carprice"));

                if(carModel.getCarName()!=null && !"".equals(carModel.getCarName())){

                    car.setCarName(highFile);  // 条查标红高亮
                }
                list1.add(car);
            }

            map1.put("total",numFound);
            map1.put("rows",list1);
            return map1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }





}
