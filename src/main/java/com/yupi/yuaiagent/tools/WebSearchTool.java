package com.yupi.yuaiagent.tools;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网页搜索工具
 */
public class WebSearchTool {

    // SearchAPI 的搜索接口地址
    private static final String SEARCH_API_URL = "https://qianfan.baidubce.com/v2/ai_search/chat/completions";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        Map<String, Object> param = new HashMap<>();

        // 消息
        param.put("messages", List.of(
                Map.of("content", query, "role", "user")
        ));

        param.put("model", "ernie-4.5-turbo-32k");


        String jsonBody = JSONUtil.toJsonStr(param);

        // 3. 发送请求
        try (HttpResponse response = HttpRequest.post(SEARCH_API_URL)
                .header("X-Appbuilder-Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(jsonBody) // 现在只接收字符串，完全正常
                .execute()) {

            // 3. 解析 JSON，提取第一个 content
            JSONObject jsonObj = JSONUtil.parseObj(response.body());
            // 从 choices 数组取第一个元素 → 取 message 对象 → 取 content
            String targetContent = jsonObj.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getStr("content");

            return targetContent;
        }catch (Exception e){
            return "Error: " + e.getMessage();
        }
    }
}
//{"choices":[{"finish_reason":"stop","index":0,"message":{"content":"### **北京热门景点全攻略**  \n北京作为千年古都，融合了历史、文化、自然与现代活力，以下景点按不同主题分类整理，助您高效规划行程：\n\n#### **一、经典历史地标**  \n| 景点名称       | 核心亮点                                                                 | 游玩建议                                                                 |\n|----------------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|\n| **故宫博物院** | 明清皇家宫殿群，2025年新增AR壁画动态还原《千里江山图》、养心殿脑机接口展^[6]^。 | 清晨7点从东华门入园避开人流，重点游览中轴线建筑群与东六宫珍宝馆^[6]^。 |\n| **天坛公园**   | 祈年殿“天圆地方”建筑典范，丹陛桥测回音壁互动性强，斋宫梨花秘境免费开放^[6]^。 | 清晨8点前入园可拍祈年殿无游客照，周边推荐方砖厂69号炸酱面^[6]^。     |\n| **颐和园**     | 皇家园林湖光山色，西堤春晓藏《西游记》彩绘故事，夜航船票含诗词投影体验^[6]^。 | 北宫门进园沿十七孔桥至佛香阁，周边聚宝源铜锅涮肉“立盘不倒”^[6]^。    |\n| **八达岭长城** | 世界奇迹级古代军事工程，4月杏花盛开时徒步北八楼至北十二楼如临画卷^[6]^。 | 乘高铁20分钟直达，避开节假日清晨登城，缆车往返120元省力观景^[6]^。   |\n\n#### **二、文化体验新地标**  \n| 景点名称               | 核心亮点                                                                 | 游玩建议                                                                 |\n|------------------------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|\n| **环球影城山海经神话区** | 全球首个山海经主题区，含共工怒触不周山4D影院、女娲补天过山车，诺金酒店享提前入园特权^[6]^。 | 园区内局气烤鸭店提供兔爷土豆泥等创意菜品，环境复刻四合院风格^[6]^。   |\n| **798艺术区**          | AI生成艺术展、3D人体扫描数字雕塑引领潮流，建筑外立面全息投影夜间灯光秀更佳^[6]^。 | 适合艺术爱好者深度游览，建议预留半天时间^[6]^。                     |\n| **恭王府**              | 和珅故居藏“福字碑”，花园假山暗藏康熙御笔“福”字，夜游项目体验胡同导览^[6]^。 | 周边南锣鼓巷雨儿胡同齐白石旧居值得探访，住宿推荐古韵坊木艺主题酒店^[6]^。 |\n\n#### **三、自然与休闲景点**  \n| 景点名称       | 核心亮点                                                                 | 游玩建议                                                                 |\n|----------------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|\n| **慕田峪长城** | 人少景美，透明泡泡屋住宿含早晚餐，AR烽火台触发古代战争场景，正关台三座敌楼连体建筑全国独有^[6]^。 | 西线徒步至箭扣长城需向导，景区附近体验四合院民宿与尹三豆汁^[6]^。     |\n| **什刹海**     | 京城水系核心，夏季划船、冬季滑冰，周边胡同藏老北京烟火气^[2][5]^。 | 建议傍晚游览，搭配周边南门涮肉或烤肉季^[2][5]^。             |\n| **香山公园**   | 秋季红叶漫山，位列“北京十六景”之一，登顶可俯瞰京城^[3]^。         | 10月下旬至11月初为最佳观赏期，建议早8点前入园避开人流^[3]^。          |\n\n#### **四、现代地标与博物馆**  \n| 景点名称       | 核心亮点                                                                 | 游玩建议                                                                 |\n|----------------|--------------------------------------------------------------------------|--------------------------------------------------------------------------|\n| **奥林匹克公园** | 鸟巢、水立方夜景灯光秀璀璨，奥林匹克塔360度俯瞰北京夜景^[6]^。   | 公园内簋街胡大饭馆24小时供应麻辣小龙虾，配燕京U8啤酒^[6]^。          |\n| **国家博物馆** | 全球最大博物馆之一，藏品超140万件，重点看“古代中国”基本陈列^[2][4][5]^。 | 需提前1-7天预约，周一闭馆^[2][4][5]^。              |\n| **军事博物馆** | 展示中国军事历史与装备，适合亲子教育与历史爱好者^[2][4][5]^。 | 免费开放，需预约，建议结合国博一日游^[2][4][5]^。    |\n\n#### **五、小众宝藏景点**  \n- **明十三陵**：世界文化遗产，神道石像生与定陵地宫值得一看^[4]^。  \n- **卢沟桥**：以“卢沟晓月”闻名，桥上501尊石狮见证历史沧桑^[3]^。  \n- **红楼大观**：以《红楼梦》为主题的园林，适合文学爱好者打卡^[3]^。  \n\n**行程规划建议**：  \n- **一日游**：故宫+景山公园+南锣鼓巷（经典历史+胡同文化）。  \n- **两日游**：Day1 长城（八达岭/慕田峪）+鸟巢夜景；Day2 颐和园+圆明园（皇家园林+遗址公园）。  \n- **三日游**：增加环球影城或798艺术区，体验现代娱乐与艺术潮流。  \n\n**实用贴士**：  \n- 故宫、国博等热门景点需提前预约，节假日建议错峰出行。  \n- 北京地铁覆盖主要景点，推荐使用“亿通行”APP扫码乘车。  \n- 春季（4-5月）赏花、秋季（9-10月）观叶为最佳旅行季节。  \n\n根据兴趣选择景点，深度感受北京的多元魅力！","role":"assistant"}}],"is_safe":true,"references":[{"content":"来北京打卡必去的几个景点,看看有没有你想去的地方 渝城百姓事 来北京打卡必去的几个景点看看有没有你想去的地方准备去北京的朋友看过来 北京必去的十二个景点 世界人民大团结中导人民利国万岁 共和国万岁看看你去过哪些 第一站故宫 第二站天安门广 /p> 旅游 旅游攻略","date":"2026-4-2","icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/quanmin-xiaoshipin.png","id":1,"image":null,"title":"全民小视频","type":"web","url":"https://quanmin.baidu.com/sv?source=share-h5&pd=qm_share_search&vid=5013812563273958620","video":null,"web_anchor":"全民小视频","website":""},{"content":"北京必去的15个地点,你去过几个 旅游推荐官小宇 大人界世 盘点北京必去的15个地方 最后一个9口%的人都错过了 中华人民共国万 第1站天安门 故室博物院第己站故宫 第3站天坛公园 第4站颐和园 第5站圆明园 第6站长城 第7站国家博物馆 第日站奥 p> 旅游 旅游攻略","date":"2026-4-2","icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/quanmin-xiaoshipin.png","id":2,"image":null,"title":"全民小视频","type":"web","url":"https://quanmin.baidu.com/sv?source=share-h5&pd=qm_share_search&vid=5395008434163377830","video":null,"web_anchor":"全民小视频","website":""},{"content":"北京十六景分别是：天安丽日、紫禁余晖、燕塞雄关、白塔堆云、颐和慧海、圜丘清音、香山红叶、十渡浮峦、盘古遗火、幽峡流碧、大钟声远、龙潭漱玉、明陵落照、卢沟狮醒、慕田古堞、红楼大观。","date":null,"icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/logo-baike.svg","id":3,"image":null,"title":"百度百科","type":"web","url":"https://baike.baidu.com/item/北京十六景/4131825","video":null,"web_anchor":"百度百科","website":""},{"content":"北京15个宝藏景点别错过 超值得逛 一直在路上 北京最值得去的 北京15个地方去的 一故宫 三天坛 四长城 五明十三陵 六奥林匹克公园 七国家博物馆 八军事博物馆 九自然博物馆 北京最值得去的 北京15个地方去的 五明十三陵 号r 礼物 一故宫 p> 旅游 旅游攻略","date":"2026-4-2","icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/quanmin-xiaoshipin.png","id":4,"image":null,"title":"全民小视频","type":"web","url":"https://quanmin.baidu.com/sv?source=share-h5&pd=qm_share_search&vid=3106064776032185167","video":null,"web_anchor":"全民小视频","website":""},{"content":"北京景点推荐,必打卡的十五个景点 北京旅游橘子 第一站天安门中华人民共和国万岁 第二站伟人纪念堂 第三站故宫 第四站八达岭长城 第五站颐和园 第六站圆明园 府王恭第七站恭王府 第八站什刹海 第九站国博 第十站天坛公园 第十一站军事博 旅游 旅游攻略","date":"2026-4-2","icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/quanmin-xiaoshipin.png","id":5,"image":null,"title":"全民小视频","type":"web","url":"https://quanmin.baidu.com/sv?source=share-h5&pd=qm_share_search&vid=5263610618402868446","video":null,"web_anchor":"全民小视频","website":""},{"content":"北京十大景点打卡指南,从故宫红墙到长城烽火台,一站式玩转京城,推荐收藏游玩 2025年北京旅游必去十大景点全攻略,涵盖故宫、长城、颐和园等经典地标与798艺术区等新兴打卡地,搭配五家特色美食与九大住宿推荐,结合购票指南与行程安排,小九带您深度体验古都北京的千年文化与现代活力。 第一站:故宫博物院——穿越明清的皇家宫殿群 清晨7点从东华门入园避开人流,重点游览中轴线建筑群与东六宫珍宝馆。2025年新...","date":"2026-4-7","icon":"https://ai-search.bj.bcebos.com/default.png","id":6,"image":null,"title":"北京旅游网","type":"web","url":"https://www.visitbeijing.com.cn/article/4QGMDJZTM9t","video":null,"web_anchor":"北京旅游网","website":""},{"content":"北京八个必去的神仙景点 环球旅游攻略68 环球旅游攻略68 北京必去的八个神仙景点 你都去过吗 故宫作为紫禁城 红墙黄瓦间封印着600年的历史 故空博物院 景山公园能俯瞰故宫全景 八达岭长城作为世界奇迹 展现古代军事防御工程智慧 /p>","date":"2026-4-3","icon":"https://appbuilder.bj.bcebos.com/baidu-search-rag-pro/icon/bilibili.ico","id":7,"image":null,"title":"哔哩哔哩","type":"web","url":"https://www.bilibili.com/video/BV1Bx8jzvECb","video":null,"web_anchor":"哔哩哔哩","website":""}],"request_id":"4aaf9abf-7794-40cb-86f7-2b1662e4f42c","safe_classification":"正常","usage":{"completion_tokens":1375,"prompt_tokens":2042,"total_tokens":3417}}

