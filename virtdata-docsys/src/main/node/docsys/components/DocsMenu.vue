<template>
  <div class="menu">
    <v-list dense>
      <v-list-group link v-for="category in categories" :key="category" value="true">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title class="text-capitalize">{{category.category}}</v-list-item-title>
          </v-list-item-content>
        </template>

        <v-list-item v-for="(doc, i) in category.docs" :key="i" link :to="doc.filename">
          <router-link :to="doc.filename">
            <v-list-item-title>{{doc.attributes.title}}</v-list-item-title>
          </router-link>
        </v-list-item>

      </v-list-group>
    </v-list>

  </div>
</template>
<script>
    export default {
        name: 'DocsMenu',
        props: ['categories']
    }
</script>
