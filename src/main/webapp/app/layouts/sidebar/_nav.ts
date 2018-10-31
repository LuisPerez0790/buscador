export const navCtranspItems = [
    {
        name: 'Sentencias',
        title: true
    },
    {
        name: 'Entidades',
        url: '/base',
        icon: 'icon-wallet',
        children: [
            {
                name: 'Sentencias',
                url: '/sentence',
                icon: 'icon-puzzle'
            },
        ]
    }
];

export const navAdminItems = [
    {
        name: 'Administración',
        title: true
    },
    {
        name: 'Admin',
        url: '/base',
        icon: 'icon-settings',
        children: [
            {
                name: 'Gateway',
                url: '/admin/gateway',
                icon: 'icon-globe'
            },
            {
                name: 'User tracker',
                url: 'admin/jhi-tracker',
                icon: 'icon-eye'
            },
            {
                name: 'Metrics',
                url: 'admin/jhi-metrics',
                icon: 'icon-chart'
            },
            {
                name: 'Health',
                url: 'admin/jhi-health',
                icon: 'icon-heart'
            },
            {
                name: 'Configuration',
                url: 'admin/jhi-configuration',
                icon: 'icon-list'
            },
            {
                name: 'Audits',
                url: 'admin/audits',
                icon: 'icon-bell'
            },
            {
                name: 'Logs',
                url: 'admin/logs',
                icon: 'icon-list'
            },
            {
                name: 'API',
                url: 'admin/docs',
                icon: 'icon-notebook'
            },
            {
                name: 'Reindexer',
                url: 'admin/elasticsearch-reindex',
                icon: 'icon-magnifier'
            }
        ]
    }
];
