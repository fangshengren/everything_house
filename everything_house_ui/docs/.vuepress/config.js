module.exports ={
    //这里面路径最开始的/是指向.vuepress/public/的
    base:'/',//部署站点的基础路径,默认/
    port: 8888,//端口
    lang: 'zh-CN',//语言设置
    title: 'WYQ雅思口语',//所有页面标题的后缀，并且在默认主题的导航栏中显示
    description: 'WYQ雅思口语',//站点描述，它会被每个页面的 Frontmatter 中的 description 字段覆盖
    head: [['link', { rel: 'icon', href: '/images/logo.PNG' }]],//站点头部的icon
    themeConfig: {
        nav : [
            { text: "首页", link: "/" },
            {
                text: 'Speaking',
                items:[
                { text: 'Part1', link: '/storeMD/part1/part1' },
                { text: 'Part2', link: '/storeMD/part2/part2' },
                { text: 'Part3', link: '/storeMD/part3/part3' }
                ]
            }
        ],
        sidebar: {
            '/storeMD/part1/': [
                'part1',
            ],

            '/storeMD/part2/': [
                'part2',
            ],

            '/storeMD/part3/': [
                'part3',
            ],

            // fallback
            '/': [
                '',        /* / */
            ]
        },
        sidebarDepth : 2,
        sidebarOpen: true,
    }
}

