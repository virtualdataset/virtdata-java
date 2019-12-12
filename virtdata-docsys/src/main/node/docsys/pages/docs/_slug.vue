<template>
    <v-app>
        <v-navigation-drawer
                v-model="drawer"
                app
        >
            <docs-menu :categories="categories"/>
        </v-navigation-drawer>

        <v-app-bar
                app
                color="secondary"
        >
            <v-app-bar-nav-icon @click.stop="drawer = !drawer"/>
            <v-toolbar-title>DS Bench Documentation</v-toolbar-title>
        </v-app-bar>

        <v-content>
            <v-container
            >
                <v-row align="stretch">

                    <div class="Doc">
                        <div class="doc-title">
                            <h1></h1>
                        </div>
                        <div class="content" v-html="doc"></div>
                    </div>

                </v-row>
            </v-container>
        </v-content>

        <v-footer
                app
                color="secondary"
        >
            <span>&copy; 2019</span>
        </v-footer>

    </v-app>
</template>
<script>
    import DocsMenu from '~/components/DocsMenu.vue'

    const fetch = require('node-fetch');
    var fm = require('front-matter');
    var MarkdownIt = require('markdown-it'),
        md = new MarkdownIt();

    export default {
        data: () => ({
            drawer: null,
        }),
        components: {
            DocsMenu
        },
        async asyncData({params}) {
            let paths = await fetch("/services/docs/markdown.csv")
                .then(res => {
                    return res.text()
                })
                .then(body => {
                    return body.split("\n")
                });

            let rawDoc = "";
            let imports = [];
            for (let index in paths) {
                let key = paths[index]
                if (key == null || key == "") {
                    continue
                }

                const [, name] = key.match(/(.+)\.md$/);
                let detailName = key.split("/").filter(x => x.includes(".md"))[0];
                detailName = detailName.substr(0, detailName.length - 3);

                //const mdMeta = resolve(key);
                let rawMD = await fetch("/services/docs/markdown/" + key)
                    .then(res => res.text())
                    .then(body => rawMD = rawMD + body)

                if (rawMD.substr(0, 9) == "undefined") {
                    rawMD = rawMD.substr(9);
                }

                var mdMeta = fm(rawMD)

                if (key.substr(0, key.length - 3) == params.slug) {
                    rawDoc = mdMeta.body
                }

                if (mdMeta.attributes == null || mdMeta.attributes.title == null) {
                    mdMeta.attributes.title = detailName;
                }
                mdMeta.categories = key.split("/").filter(x => !x.includes("."));
                mdMeta.filename = "/docs/" + encodeURIComponent(name);
                console.log(key);
                imports.push(mdMeta);
            }
            const categorySet = new Set()
            imports.forEach(x => {
                categorySet.add(x.categories.toString())
            })

            const categories = Array.from(categorySet).map(category => {
                    const docs = imports.filter(x => {
                        return x.categories.toString() == category
                    })
                    return {category, docs}
                }
            )

            try {
                const doc = md.render(rawDoc);
                return {
                    doc: doc,
                    categories: categories,
                }
            } catch (err) {
                return false
            }
        }
    }
</script>
