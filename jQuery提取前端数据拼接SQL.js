// 直接将此脚本输入网页控制台即可获取拼接后的sql
$(function () {
    var sql = "INSERT INTO 数据库.具体表 (id, archived, import_id, import_origin, stamp, version, created_on, updated_on, access_num, attrs, coverage_time, end_date, industries, like_num, media_platforms, name, permission, report_link, report_types, start_date, created_by, updated_by, audience, cover_text, cover, owner, audience_type, html) " +
        "VALUES ";

    // 映射关系
    var reportTypeMapping = {
        '行业洞察': 'industry_insights',
        '市场报告': 'market_report',
        '投放诊断': 'campaign_diagnosis',
        '素材推荐': 'creative_recommendations',
        '媒体资讯': 'media_news',
        '投放指南': 'campaign_guide'
    };

    var industryMapping = {
        '电商': 'ecommerce',
        '游戏': 'gaming',
        '应用': 'apps'
    };

    var platformMapping = {
        'Facebook': 'facebook',
        'Google': 'google',
        'TikTok': 'tiktok',
        'Kwai': 'kwai',
        'Snapchat': 'snapchat',
        'Twitter': 'twitter',
        '华为': 'huawei',
        'BING': 'bing'
    };

    var reportFrequencyMapping = {
        '日度': 'daily',
        '周度': 'weekly',
        '月度': 'monthly',
        '季度': 'quarterly',
        '年度': 'annual',
        '自定义': 'custom'
    };

    var accessLevelMapping = {
        '个人可见': 'private',
        '团队可见': 'team',
        '全员共享': 'company',
        '指定人员可见': 'specified'
    };


    $('table').find('tr').each(function (i, n) {
        if (i === 0) return; // 跳过表头
        let tds = $(n).children('td');
        let td0 = tds.eq(0); // name
        let td1 = tds.eq(1); // report_types
        let td2 = tds.eq(2); // industries
        let td3 = tds.eq(3); // media_platforms
        let td4 = tds.eq(4); // coverage_time
        let td6 = tds.eq(6); // report_link
        let td9 = tds.eq(9); // permission

        // const currentDate = new Date();
        // const stamp = currentDate.toISOString().slice(0, 19).replace('T', ' ');
        // const created_on = currentDate.toISOString().slice(0, 26).replace('T', ' ');

        let name = td0.text().trim() || 'NULL';
        let report_types = td1.text().split(',').map(type => reportTypeMapping[type.trim()] || 'NULL').join(',').replace(/\n/g, '');
        let industries = industryMapping[td2.text()] || 'NULL';
        let media_platforms = td3.text().split(',').map(platform => platformMapping[platform.trim()] || 'NULL').join(',').replace(/\n/g, '');
        let coverage_time = reportFrequencyMapping[td4.text()] || 'NULL';
        let report_link = td6.find('a').attr('href') || 'NULL';
        let permission = accessLevelMapping[td9.text()] || 'NULL';

        // 不为NULL的时候才拼接属性，否则拼接空字符串
        name = name !== 'NULL' ? "'" + name + "'" : name;
        report_types = report_types !== 'NULL' ? "'" + report_types + "'" : report_types;
        industries = industries !== 'NULL' ? "'" + industries + "'" : industries;
        media_platforms = media_platforms !== 'NULL' ? "'" + media_platforms + "'" : media_platforms;
        coverage_time = coverage_time !== 'NULL' ? "'" + coverage_time + "'" : coverage_time;
        report_link = report_link !== 'NULL' ? "'" + report_link + "'" : report_link;
        permission = permission !== 'NULL' ? "'" + permission + "'" : permission;
        // console.log(media_platforms);

        let id = i + 31;  // id从31开始自增

        // 拼接sql
        sql += "(" + id + ", NULL, NULL, NULL, CURRENT_TIMESTAMP, 0,NULL, NULL, NULL, " +
            "NULL, " + coverage_time + ", NULL, " + industries + ", NULL, " + media_platforms + ", " +
            name + ", " + permission + ", " + report_link + ", " + report_types + ", NULL, 1, NULL, NULL, " +
            "NULL, NULL, NULL, NULL, NULL),";
    });

    sql = sql.slice(0, -1) + ";"; // 去掉尾部逗号，末尾加分号
    sql = sql.replace(/(\r\n|\n|\r)/gm, ' ');  // 去掉换行符
    console.log(sql);

});
