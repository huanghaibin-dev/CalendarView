package com.haibin.calendarviewproject;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.haibin.calendarviewproject.group.GroupRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;

    public ArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日推荐", create(0));
        map.put("每周热点", create(1));
        map.put("最高评论", create(2));
        titles.add("今日推荐");
        titles.add("每周热点");
        titles.add("最高评论");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextTitle.setText(item.getTitle());
        h.mTextContent.setText(item.getContent());
        mLoader.load(item.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into(h.mImageView);
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle,
                mTextContent;
        private ImageView mImageView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }


    private static Article create(String title, String content, String imgUrl) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setImgUrl(imgUrl);
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0) {
            list.add(create("新西兰克马德克群岛发生5.7级地震 震源深度10千米",
                    "#地震快讯#中国地震台网正式测定：12月04日08时08分在克马德克群岛（南纬32.82度，西经178.73度）发生5.7级地震，震源深度10千米。",
                    "http://cms-bucket.nosdn.127.net/catchpic/2/27/27e2ce7fd02e6c096e21b1689a8a3fe9.jpg?imageView&thumbnail=550x0"));
            list.add(create("俄罗斯喊冤不当\"背锅侠\" 俄美陷入\"后真相\"旋涡",
                    "“差到令人震惊”，但不怪特朗普。俄罗斯总理德米特里·梅德韦杰夫近日在谈到俄美关系时这样说。俄罗斯近来连遭美国“恶毒”指责和西方国家连环出击。一些国际舆论认为，俄罗斯成了“背锅侠”，俄罗斯自己也公开喊冤斥责美国。在俄美你来我往的互掐中，真相似乎变得已不那么重要了。",
                    "http://cms-bucket.nosdn.127.net/catchpic/c/c8/c8b0685089258b82f3ca1997def78d8d.png?imageView&thumbnail=550x0"));
            list.add(create("中企投资巴西获支持 英媒:巴西人感激\"保住饭碗\"",
                    "参考消息网12月4日报道 英媒称，里约热内卢附近的阿苏港曾被埃克·巴蒂斯塔称为“通往中国的公路”，10多年前，这位现已名誉扫地的巴西前首富创建了这个超级港，大宗商品热潮结束后，他在巴西的商业帝国几乎无一幸存并于2014年破产，但此后至今有一个项目仍蓬勃发展，那就是阿苏港。",
                    "http://cms-bucket.nosdn.127.net/catchpic/8/8b/8ba2d19b7f63efc5cf714960d5edd2c3.jpg?imageView&thumbnail=550x0"));
            list.add(create("美电视台记者因误报有关弗林新闻被停职四周",
                    "【环球网报道】据俄罗斯卫星网12月3日报道，美国ABC电视台记者布莱恩·罗素因在有关美国总统前国家安全顾问迈克尔·弗林的新闻报道中的失误，临时被停职。",
                    "http://cms-bucket.nosdn.127.net/5d18566fde70407b9cc3a728822115c320171203133214.jpeg?imageView&thumbnail=550x0"));
            list.add(create("预计明年3月上市 曝全新奥迪Q5L无伪谍照",
                    "随着之前全新一代国产奥迪Q5L在工信部目录亮相，最近曝光的测试车也都基本褪去了伪装，不过正式上市还是要等到2018年3月份。从最新曝光的内饰来看，轴距的加长令后排的空间有着非常明显的提升。",
                    "http://cms-bucket.nosdn.127.net/eda9ca222352470190c4f0d6b9a8c29420171201160854.jpeg?imageView&thumbnail=550x0"));
        } else if (p == 1) {
            list.add(create(
                    "2019年投产 电咖整车生产基地落户浙江绍兴",
                    "网易汽车11月30日报道 两周前的广州车展上，电咖发布了其首款电动汽车EV10，官方指导价为13.38万-14.18万，扣除补贴后的零售价为5.98万元-6.78万元，性价比很高。抛开车辆本身，引起业界关注的是这家新势力造车企业的几位核心成员，当年上汽大众团队的三位老兵--张海亮、向东平、牛胜福携手用了957天造了一辆可以上市的车。",
                    "http://cms-bucket.nosdn.127.net/674c392123254bb69bdd9227442965eb20171129203658.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "2017年进入尾声，苹果大笔押注的ARkit还好么？",
                    "谷歌推出了AR眼镜、ARCore平台和应用在手机上的Project Tango，Facebook也上线了AR开发平台和工具。至于苹果，更是把AR当做发展的重中之重。在新品iPhone8和iPhoneX中，后置摄像头专为AR进行校准，前置摄像头还添加了可以带来更好AR效果的深度传感器。",
                    "http://cms-bucket.nosdn.127.net/catchpic/7/76/76135ac5d3107a1d5ba11a8ee2fc7e27.jpg?imageView&thumbnail=550x0"));
            list.add(create(
                    "亚马逊CTO：我们要让人类成为机器人的中心！",
                    "那些相信应用下载会让世界变得更美好的智能手机布道者和应用爱好者们，会在AWS re:Invent大会上感到不自在。亚马逊网络服务首席技术官Werner Vogels表示，所有这些都未能实现信息的民主化。",
                    "http://cms-bucket.nosdn.127.net/ddb758f16a7d4aa3aa422ec385fc3e5020171204081818.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "有特斯拉车主想用免费的充电桩挖矿，但这可能行不通",
                    "在社交网络 Facebook 上的一个特斯拉车主群组中，有人开脑洞说可以尝试自己组装矿机放在特斯拉后备箱中，接入车载电池的电源，然后将车停到超级充电桩附近，就能用免费获得的电力挖矿了。",
                    "http://crawl.nosdn.127.net/nbotreplaceimg/4ce9c743e6c02f6777d22278e2ef8bc3/2b33e32532db204fe207693c82719660.jpg"));
        } else if (p == 2) {
            list.add(create(
                    "2017年进入尾声，苹果大笔押注的ARkit还好么？",
                    "谷歌推出了AR眼镜、ARCore平台和应用在手机上的Project Tango，Facebook也上线了AR开发平台和工具。至于苹果，更是把AR当做发展的重中之重。在新品iPhone8和iPhoneX中，后置摄像头专为AR进行校准，前置摄像头还添加了可以带来更好AR效果的深度传感器。",
                    "http://cms-bucket.nosdn.127.net/catchpic/7/76/76135ac5d3107a1d5ba11a8ee2fc7e27.jpg?imageView&thumbnail=550x0"));
            list.add(create(
                    "亚马逊CTO：我们要让人类成为机器人的中心！",
                    "那些相信应用下载会让世界变得更美好的智能手机布道者和应用爱好者们，会在AWS re:Invent大会上感到不自在。亚马逊网络服务首席技术官Werner Vogels表示，所有这些都未能实现信息的民主化。",
                    "http://cms-bucket.nosdn.127.net/ddb758f16a7d4aa3aa422ec385fc3e5020171204081818.jpeg?imageView&thumbnail=550x0"));
            list.add(create("中企投资巴西获支持 英媒:巴西人感激\"保住饭碗\"",
                    "参考消息网12月4日报道 英媒称，里约热内卢附近的阿苏港曾被埃克·巴蒂斯塔称为“通往中国的公路”，10多年前，这位现已名誉扫地的巴西前首富创建了这个超级港，大宗商品热潮结束后，他在巴西的商业帝国几乎无一幸存并于2014年破产，但此后至今有一个项目仍蓬勃发展，那就是阿苏港。",
                    "http://cms-bucket.nosdn.127.net/catchpic/8/8b/8ba2d19b7f63efc5cf714960d5edd2c3.jpg?imageView&thumbnail=550x0"));
            list.add(create("美电视台记者因误报有关弗林新闻被停职四周",
                    "【环球网报道】据俄罗斯卫星网12月3日报道，美国ABC电视台记者布莱恩·罗素因在有关美国总统前国家安全顾问迈克尔·弗林的新闻报道中的失误，临时被停职。",
                    "http://cms-bucket.nosdn.127.net/5d18566fde70407b9cc3a728822115c320171203133214.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "2019年投产 电咖整车生产基地落户浙江绍兴",
                    "网易汽车11月30日报道 两周前的广州车展上，电咖发布了其首款电动汽车EV10，官方指导价为13.38万-14.18万，扣除补贴后的零售价为5.98万元-6.78万元，性价比很高。抛开车辆本身，引起业界关注的是这家新势力造车企业的几位核心成员，当年上汽大众团队的三位老兵--张海亮、向东平、牛胜福携手用了957天造了一辆可以上市的车。",
                    "http://cms-bucket.nosdn.127.net/674c392123254bb69bdd9227442965eb20171129203658.jpeg?imageView&thumbnail=550x0"));
        }


        return list;
    }
}
