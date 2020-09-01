package com.example.administrator.yunyue.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.utils.BoldSearchUtils;

public class JyxlsActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_jyx_back;
    private TextView tv_jyxls1, tv_jyxls2, tv_jyxls3, tv_jyxls4, tv_jyxls5, tv_jyxls6;
    private String sTv1 = "&nbsp;&nbsp;&nbsp;&nbsp;公元<b><tt>946</tt></b>年一位从军卸甲青年，尊铭祖辈父母教诲，率弟妹亲属部下等百余人，隐姓埋名，秘密迁徙至祖籍故里（今山西省临汾市襄汾县赵康镇－赵氏孤儿故事之发祥地）隐居。他们在此置办田产、开荒种地、行医采药、购粮纳草、招兵买马、安置军属、广纳贤才；开设了银号商铺、医堂药房、加工作坊等，取“济元祥”为号。产业很快遍及山西晋东南和晋中等地。该青年即为济元祥之鼻祖、自称<b><tt>“创行者”</tt></b>的<b><tt>赵匡济</tt></b>，又名<b><tt>光济</tt></b>。他谦卑谨慎，励志有为；纵横捭阖，鸿业远图。立于悬壶济世，志于文教兴国。";
    private String sTv2 = "&nbsp;&nbsp;&nbsp;&nbsp;公元<b><tt>949</tt></b>年至公元<b><tt>959</tt></b>年，<b><tt>创行者</tt></b>创立了以道家文化思想为内涵的治国理念，践行了封建社会国家意识形态领域的文化思想，建立了以道家养生文化理论为基础的“治国济康业老君堂”（以下简称：<b><tt>“老君堂”</tt></b>）。以道教三清信仰理论教化人们应坚守为人处事的底线，使人相信“人在做天在看”的神无处不在。<b><tt>创行者</tt></b>把治国与百姓健康（人体健康和思想健康）贯穿于<b><tt>“老君堂”</tt></b>文化的始终，取得了丰硕的文化积累，推动了“老君堂”产业的规范发展和当时社会与经济的进步，使得这种意识形态领域的文化思想在各领域繁衍生息。<b><tt>“老君堂”</tt></b>融纳了千年根深蒂固之儒释道文化并形成自己的独特思想，其根本理念就是：民不畏死奈何以死惧之？溥天率土，人心所归，惟道与义。民心所向，胜之所往。以此道教文化和养生文化奠定了<b><tt>“老君堂”</tt></b>文化的基础，并传承至今。&nbsp;&nbsp;&nbsp;&nbsp;历史实践经验证明，我们的祖先通过那个战乱年代，非常智慧的认识到：人民健康才是治国之本，文化信仰实乃强国之魂。";
    private String sTv3 = "中华民族历经千年历史文化长河的不断推陈出新，<b><tt>“老君堂”</tt></b>早已成为了历史遗迹；但留在民间的<b><tt>“济元祥”</tt></b>道德文化之魂魄，仍在传颂中生生不息。&nbsp;&nbsp;&nbsp;&nbsp;据考证，早期民间称医者为“郎中”， 又称“巫医”、“巫师”；而宫廷称呼为“御医”、“太医”。<b><tt>创行者</tt></b>为规范和提升医患称谓的文明推进，在<b><tt>“老君堂”</tt></b>招收医师学徒的培训中，称医者为“医生”“大夫”，称患者为“先生”、“夫人”，这样的尊称使民间所谓的“郎中”“巫医”“巫师”“病人”等不雅称谓逐渐得以改善，融合形成了医患之间平等的社会地位，显现了信仰文化促进社会文明进步之魅力。&nbsp;&nbsp;&nbsp;&nbsp;据考证，在五代十国战乱期末至宋初，经<b><tt>“老君堂”</tt></b>培训授封的大药房达数千家，拜<b><tt>“老君堂”</tt></b>高师授教，砺徒学成：观天察地、四季五行、节气风水、识草知性，熟懂炮制、巫术占卜、望闻问切、点穴把脉、疏通经络、针灸拔罐、抓药灵妙、岐黄之术者，乃可为大夫，方可授号出师，授封为<b><tt>“老君堂”</tt></b>下的一代二代三代大药房，冠名“同济堂”、“济同堂”等带有“济”字的诸多大药房。并由<b><tt>济元祥实业</tt></b>出资建堂，例如：大堂三室（诊断室、休沐室、寝室），一院五馆（医馆、食馆、针灸馆、养生馆、武馆），三房（柜房、丹房、药房）等。后经发展医馆药馆分行，医者只看病开方，药者只炮制抓药。以封号称谓，如：“济元堂”或“元济堂”大药房等。人们从授封的字号名称中，便可知其医者亦或药者？为授封于哪位大师或长老的门别？论资排辈，悬壶（葫芦）授匾。每个大药房的开立，均由“老君堂”主持授封加持仪式，当众宣封授号，将由老君堂堂主或长老书写的悬挂标识（一个特别的葫芦和花边挂旗）进行封授。带有“济”字号的医药堂，都得其独特秘籍方剂，各有其绝技特长。如：“济元堂大药房、宏济堂大药房”等，都是一脉相承之。被“老君堂”授封的几千号大药房，仍然在传承中为百姓服务（详见附后字号名录）。&nbsp;&nbsp;&nbsp;&nbsp;据史料记载，至公元<b><tt>１２７３</tt></b>年由于朝代更迭，当政者忌讳前朝百姓祭拜<b><tt>“治国济康业老君堂”</tt></b>，恐其成为造反复辟者之集聚场所，视“治国济康业”为“敏感”字而被取缔。但民间<b><tt>“老君堂”</tt></b>历史所遗留下来的济元祥文化，在千年历史文化长河的不断流淌之中沉淀结晶，奠定了中华民族之声声不息具有丰富文化内涵元素的特色土壤；成就了无数民间文学艺术大师，促使其成为创作历史故事题材和繁衍文明历史的文化之根（宋词元曲之魂），在文明交融的健康文化生活中不断开花结果，时至今日仍然经典传唱！";
    private String sTv4 = "&nbsp;&nbsp;&nbsp;&nbsp;清末民初，<b><tt>创行者</tt></b>第55代传人赵度庆携子赵光魁，承先祖遗志，秉济元祥博爱济世，普济大众之魂元，山西济元祥票号、典当、药房、商铺已发展到全国很多地方，有：河北、河南、山西、内蒙古西口、四川、西北几省等；各省城均开设分号，如典当行、大药房、加工作坊等。其涉足领域广泛，生意兴隆，促进了当时社会经济的发展和繁荣。为培养各类人才，开办了私塾学堂，以<b><tt>“老君堂”</tt></b>传承的教养文化和思想理念，制定教学纲要，主推健康强国、习武保国、文化治国、诚信立国的国学文化教学课程。经<b><tt>济元祥</tt></b>出资培养的大批爱国优秀人士和经商人才，促进夯实了晋商文化的底蕴，弘扬了国学文化在旧中国封建社会的育人普世价值观。同时也推动和注入了晋商人的经营思想和文化理念之魂，促进了晋商“天下第一”的文化品牌效应和经久不衰的社会名气。";
    private String sTv5 = "&nbsp;&nbsp;&nbsp;&nbsp;正如习近平总书记指出，“中医药学凝聚着深邃的哲学智慧和中华民族几千年的健康养生理念及其实践经验，是中国古代科学的瑰宝，也是打开中华文明宝库的钥匙”。中医药作为非物质文化遗产（简称“非遗”）是我国璀璨的历史文化发展长河中最具代表性的内容。&nbsp;&nbsp;&nbsp;&nbsp;传统医药是非物质文化遗产代表性项目十大类别之一。如果说中医药文化是我国传统文化的代表，那么中医药非物质文化遗产就是传统文化里的“遗珠”。然而，在已公布的四批1372项国家级非遗项目中，传统医药只有33项，占很少的一部分，与中医药在我国传统文化中的优势地位很不相称。如何把先祖遗留下来的中医药文化和被遗弃散落在民间的中医药秘籍配方，用科学的方法搜集挖掘出来，更加充分合理利用，展现发挥其原有的资源优势；利用当代先进的科学技术条件，应用先进的网络技术为中国的健康产业和老龄化社会服务，是践行社会主义核心价值观的体现；也是当代<b><tt>“济元祥”</tt></b>再生再现其精神魅力的又一次历史机会。";
    private String sTv6 = "&nbsp;&nbsp;&nbsp;&nbsp;通过申遗可以使更多的人认识到本门派的中医药文化传承，提升知名度、美誉度，从而能在更大范围、更高层面保护我们的中医药文化遗产，在加强保护与传承的同时，发挥非遗的社会功能和文化价值，是值得我们思考和研究的事情。民间中医术世代传承，这种传承脉络就像一部“家谱”，在传承过程中积淀下来大量资料、书籍、器具等，成为这一流派的身份证明，成为其区别于其他中医药流派特有的、不凡的特殊价值所在。申请非物质文化遗产成功，等同于获得了国家有关部门的认可。这不仅是对申请者世代传承心血的肯定，让许多<b><tt>“老君堂”</tt></b>民间中医药人获得身份归属，也是展现本中医药流派的文化自信和自豪，需要代代传承下去。目前要把<b><tt>“济元祥”</tt></b>中医药文化项目的申遗放到经营工作的重点上，从实践应用中来，必将回归到实践应用中去，所以济元祥（北京）实业股份有限公司，寻求全国<b><tt>“老君堂”</tt></b>门下同堂合作伙伴，并要争取国家科研机构的学术支持，征得到国家和地方的政策支持。开办<b><tt>“老君堂”</tt></b>传习培训中心、培养传承人，搭建一个强化和统一本门派历史文化资源的平台；学术经验交流和健康养老信息平台，促进健康养老产业在济元祥中医药文化发展中的发扬光大。&nbsp;&nbsp;&nbsp;&nbsp;“传统中医药”是我国非物质文化遗产名录十大类别中的一个重要类别, 非物质文化遗产概念的提出, 为人们提供了解读传统中医药及其文化的新视角, 其历史价值、文化价值和社会价值得到更多的重视。济元祥人知道：一滴水，只有融入大海，才能永远不会干涸；一脉文化，只有走向世界，才能不断发扬光大。";

    private TextView tv_gw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_jyxls);
        ll_jyx_back = findViewById(R.id.ll_jyx_back);
        ll_jyx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_jyxls1 = findViewById(R.id.tv_jyxls1);
        tv_jyxls1.setText(Html.fromHtml(sTv1));
        tv_jyxls2 = findViewById(R.id.tv_jyxls2);
        tv_jyxls2.setText(Html.fromHtml(sTv2));
        tv_jyxls3 = findViewById(R.id.tv_jyxls3);
        tv_jyxls3.setText(Html.fromHtml(sTv3));
        tv_jyxls4 = findViewById(R.id.tv_jyxls4);
        tv_jyxls4.setText(Html.fromHtml(sTv4));
        tv_jyxls5 = findViewById(R.id.tv_jyxls5);
        tv_jyxls5.setText(Html.fromHtml(sTv5));
        tv_jyxls6 = findViewById(R.id.tv_jyxls6);
        tv_jyxls6.setText(Html.fromHtml(sTv6));
        tv_gw = findViewById(R.id.tv_gw);
        tv_gw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JyxlsActivity.this, GuanggaoActivity.class);
                intent.putExtra("link", "https://sxjiyuanxiang.com/");
                startActivity(intent);
            }
        });

    }
}
