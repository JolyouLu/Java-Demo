import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import top.jolyoulu.mybatis.entity.User;

import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2021/9/1 13:19
 * @Version 1.0
 */
public class ESDocApiTest {

    RestHighLevelClient esClient;

    @Before
    public void connect(){
        //创建ES客户端
        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

    @After
    public void close() throws IOException {
        //关闭客户端
        esClient.close();
    }

    @Test
    public void creatDoc() throws IOException {
        //创建Doc
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("user").id("1001");
        User user = new User();
        user.setName("张三");
        user.setAge(30);
        user.setSex("男");
        indexRequest.source(JSONObject.toJSONString(user), XContentType.JSON);

        IndexResponse response = esClient.index(indexRequest, RequestOptions.DEFAULT);
        //响应状态
        System.out.println(response.getResult());
    }

    @Test
    public void updateDoc() throws IOException {
        //修改数据
        UpdateRequest request = new UpdateRequest();
        request.index("user").id("1001");
        request.doc(XContentType.JSON,"sex","女");

        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        //响应状态
        System.out.println(response.getResult());
    }

    @Test
    public void getDoc() throws IOException {
        //查询数据
        GetRequest request = new GetRequest();
        request.index("user").id("1001");

        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        //打印结果
        System.out.println(response.getSourceAsString());
    }

    @Test
    public void deleteDoc() throws IOException {
        //删除
        DeleteRequest request = new DeleteRequest();
        request.index("user").id("1001");

        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
        //打印结果
        System.out.println(response.toString());
    }

    //批量操作====================================================

    @Test
    public void batchCreatDoc() throws IOException {
        //批量插入数据
        BulkRequest request = new BulkRequest();
        //循环装入数据
        String[] names = {"张三","李四","王五","赵六","张大炮","老王","张三1","张三2","张三3","张三4","张三5"};
        for (int i = 0; i < 10; i++) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index("user").id("100"+i);
            User user = new User(names[i],"男",18+i);
            indexRequest.source(JSONObject.toJSONString(user),XContentType.JSON);
            request.add(indexRequest);
        }
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(response.getItems());
    }
    @Test
    public void batchDeleteDoc() throws IOException {
        //批量插入删除
        BulkRequest request = new BulkRequest();
        //循环装入数据
        for (int i = 0; i < 10; i++) {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.index("user").id("100"+i);
            request.add(deleteRequest);
        }
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
        System.out.println(response.getItems());
    }
}
