function fn() {

    var env = karate.env;
    karate.log('api.karate.env system property was:', env);

    if (!env) {
        env = 'prod';
    }

    var config = {
        env: env,
        category: '/v1/menu/<menu_id>/category',
        status: '/health',
        menu: '/v1/menu',
        menu_v2: '/v2/menu',
        items: '/items',
        item_by_id_path: '/v1/menu/<menu_id>/category/<category_id>/items/<item_id>',
        category_by_id_path: '/v1/menu/<menu_id>/category/<category_id>',
        menu_by_id_path: '/v1/menu/<menu_id>',

        menu_already_exists: 'A Menu with the name \'<menu_name>\' already exists for the restaurant with id \'<tenant_id>\'.',
        menu_does_not_exists: 'A menu with id \'<menu_id>\' does not exist.',
        category_does_not_exists: 'A category with the id \'<category_id>\' does not exist for menu with id \'<menu_id>\'.',
        category_already_exists: 'A category with the name \'<category_name>\' already exists for the menu with id \'<menu_id>\'.',
        item_already_exists: 'An item with the name \'<item_name>\' already exists for the category \'<category_id>\' in menu with id \'<menu_id>\'.',
        item_does_not_exists: 'An item with the id \'<item_id>\' does not exists for category with the id \'<category_id>\' and for menu with id \'<menu_id>\'.',
    };

    var menu_body = {
        "tenantId": "",
        "name": "",
        "description": "",
        "enabled": null
    };

    var category_body = {
        "name": "",
        "description": ""
    };

    var item_body = {
        "name": "",
        "description": "",
        "price": 0,
        "available": null
    };

    var oauth_body = {
        "client_id": "",
        "client_secret": "",
        "audience": "",
        "grant_type": ""
    };

    if (env == 'local') {
        config.base_url = 'http://localhost:9000';
    } else if (env == 'prod') {
        config.base_url = 'https://prod-java-api.prod.amidostacks.com/api';
    }

    return config;
}
