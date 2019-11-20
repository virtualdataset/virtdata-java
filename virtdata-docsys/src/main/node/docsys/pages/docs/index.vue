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
      color= "secondary"
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>DS Bench Documentation</v-toolbar-title>
    </v-app-bar>

    <v-content>
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
  var fm = require('front-matter')

  export default {
    data: () => ({
      drawer: true,
    }),
    components: {
        DocsMenu
    },
    async asyncData() {
      let paths = await fetch("/markdown.csv")
        .then(res => {
          return res.text()
        })
        .then(body => {
          return body.split("\n")
        });

      let imports = [];
      for (let index in paths){
        let key = paths[index]
        if (key == null || key == "") {
          continue
        }
        const [, name] = key.match(/\/(.+)\.md$/);
        let detailName = key.split("/").filter(x => x.includes(".md"))[0];
        detailName = detailName.substr(0, detailName.length -3);

        //const mdMeta = resolve(key);
        let rawMD = await fetch("/markdown"+key.substr(1))
          .then(res => res.text())
          .then(body => rawMD = rawMD + body)

        var mdMeta = fm(rawMD)
        
        if (mdMeta.attributes == null || mdMeta.attributes.title == null){
            mdMeta.attributes.title = detailName;
        }
        mdMeta.categories = key.split("/").filter(x => !x.includes("."));
        mdMeta.filename = "/docs/"+encodeURIComponent(name);
        console.log(key);
        imports.push(mdMeta);
      }
      const categorySet = new Set()
      imports.forEach(x => {
        categorySet.add(x.categories.toString())
      })

      const categories = Array.from(categorySet).map(category =>
        {
          const docs = imports.filter(x => {
              return x.categories.toString() == category
          })
          return {category,docs}
        }
      )
      return {
        categories: categories,
      }
    },
  }

</script>
