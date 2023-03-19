import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2021/9/1 13:19
 * @Version 1.0
 */
public class ESQueryTest {

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
    public void queryAll() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void termQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //条件查询
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("age",18));

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void pageQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //分页查询
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.from(0);
        builder.size(2);

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void orderByQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //排序
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.sort("age", SortOrder.DESC);

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void filQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //过滤结果
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        //只显示
        String[] includes = {"name"};
        //排除
        String[] excludes = {};
        builder.fetchSource(includes,excludes);

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void andQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //组合查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("age",19));
        boolQuery.must(QueryBuilders.matchQuery("sex","男"));

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(boolQuery);

        request.source(builder);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void betweenQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //范围查询
        RangeQueryBuilder age = QueryBuilders.rangeQuery("age");
        age.gte(18);
        age.lte(20);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(age);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void likeQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //模糊查询
        FuzzyQueryBuilder fuzziness = QueryBuilders
                .fuzzyQuery("name", "张三")
                .fuzziness(Fuzziness.ONE);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(fuzziness);
        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void highlightQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //查询结果高亮
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermsQueryBuilder query = QueryBuilders.termsQuery("name", "张");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='rea'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name");

        builder.highlighter(highlightBuilder);
        builder.query(query);

        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getHighlightFields());
        }
    }

    @Test
    public void groupByQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //聚合查询
        SearchSourceBuilder builder = new SearchSourceBuilder();
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .max("maxAge")
                .field("age");

        builder.aggregation(aggregationBuilder);

        request.source(builder);

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        //遍历结果
        for (SearchHit hit: hits){
            System.out.println(hit.getSourceAsString());
        }
    }
}
