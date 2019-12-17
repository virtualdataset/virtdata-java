(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[4],{

/***/ 207:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";

// CONCATENATED MODULE: ./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./components/DocsMenu.vue?vue&type=template&id=7e610957&
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"menu"},[_c('v-list',{attrs:{"dense":""}},_vm._l((_vm.categories),function(category){return _c('v-list-group',{key:category,attrs:{"link":""},scopedSlots:_vm._u([{key:"activator",fn:function(){return [_c('v-list-item-content',[_c('v-list-item-title',{staticClass:"text-capitalize"},[_vm._v(_vm._s(category.category))])],1)]},proxy:true}],null,true)},[_vm._v(" "),_vm._l((category.docs),function(doc,i){return _c('v-list-item',{key:i,attrs:{"link":"","href":doc.filename}},[_c('router-link',{attrs:{"to":doc.filename}},[_c('v-list-item-title',[_vm._v(_vm._s(doc.attributes.title))])],1)],1)})],2)}),1)],1)}
var staticRenderFns = []


// CONCATENATED MODULE: ./components/DocsMenu.vue?vue&type=template&id=7e610957&

// CONCATENATED MODULE: ./node_modules/babel-loader/lib??ref--2-0!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./components/DocsMenu.vue?vue&type=script&lang=js&
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
/* harmony default export */ var DocsMenuvue_type_script_lang_js_ = ({
  name: 'DocsMenu',
  props: ['categories']
});
// CONCATENATED MODULE: ./components/DocsMenu.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_DocsMenuvue_type_script_lang_js_ = (DocsMenuvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./node_modules/vue-loader/lib/runtime/componentNormalizer.js
var componentNormalizer = __webpack_require__(38);

// EXTERNAL MODULE: ./node_modules/vuetify-loader/lib/runtime/installComponents.js
var installComponents = __webpack_require__(188);
var installComponents_default = /*#__PURE__*/__webpack_require__.n(installComponents);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VList/VList.js
var VList = __webpack_require__(211);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VList/VListGroup.js + 3 modules
var VListGroup = __webpack_require__(235);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VList/VListItem.js
var VListItem = __webpack_require__(198);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VList/index.js + 7 modules
var components_VList = __webpack_require__(179);

// CONCATENATED MODULE: ./components/DocsMenu.vue





/* normalize component */

var component = Object(componentNormalizer["a" /* default */])(
  components_DocsMenuvue_type_script_lang_js_,
  render,
  staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var DocsMenu = __webpack_exports__["a"] = (component.exports);

/* vuetify-loader */






installComponents_default()(component, {VList: VList["a" /* default */],VListGroup: VListGroup["a" /* default */],VListItem: VListItem["a" /* default */],VListItemContent: components_VList["a" /* VListItemContent */],VListItemTitle: components_VList["b" /* VListItemTitle */]})


/***/ }),

/***/ 380:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: ./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./pages/docs/index.vue?vue&type=template&id=1021cffe&
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('v-app',[_c('v-navigation-drawer',{attrs:{"app":""},model:{value:(_vm.drawer),callback:function ($$v) {_vm.drawer=$$v},expression:"drawer"}},[_c('docs-menu',{attrs:{"categories":_vm.categories}})],1),_vm._v(" "),_c('v-app-bar',{attrs:{"app":"","color":"secondary"}},[_c('v-app-bar-nav-icon',{on:{"click":function($event){$event.stopPropagation();_vm.drawer = !_vm.drawer}}}),_vm._v(" "),_c('v-toolbar-title',[_vm._v("DS Bench Documentation")])],1),_vm._v(" "),_c('v-content'),_vm._v(" "),_c('v-footer',{attrs:{"app":"","color":"secondary"}},[_c('span',[_vm._v("© 2019")])])],1)}
var staticRenderFns = []


// CONCATENATED MODULE: ./pages/docs/index.vue?vue&type=template&id=1021cffe&

// EXTERNAL MODULE: ./node_modules/core-js/modules/es7.array.includes.js
var es7_array_includes = __webpack_require__(52);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.string.includes.js
var es6_string_includes = __webpack_require__(53);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.regexp.match.js
var es6_regexp_match = __webpack_require__(90);

// EXTERNAL MODULE: ./node_modules/@babel/runtime/helpers/esm/slicedToArray.js + 3 modules
var slicedToArray = __webpack_require__(15);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.array.from.js
var es6_array_from = __webpack_require__(124);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.regexp.to-string.js
var es6_regexp_to_string = __webpack_require__(55);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.date.to-string.js
var es6_date_to_string = __webpack_require__(56);

// EXTERNAL MODULE: ./node_modules/core-js/modules/web.dom.iterable.js
var web_dom_iterable = __webpack_require__(13);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.object.to-string.js
var es6_object_to_string = __webpack_require__(7);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.string.iterator.js
var es6_string_iterator = __webpack_require__(57);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.set.js
var es6_set = __webpack_require__(208);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.regexp.split.js
var es6_regexp_split = __webpack_require__(54);

// EXTERNAL MODULE: ./node_modules/regenerator-runtime/runtime.js
var runtime = __webpack_require__(58);

// EXTERNAL MODULE: ./components/DocsMenu.vue + 4 modules
var DocsMenu = __webpack_require__(207);

// CONCATENATED MODULE: ./node_modules/babel-loader/lib??ref--2-0!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./pages/docs/index.vue?vue&type=script&lang=js&













//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//


var fetch = __webpack_require__(218);

var fm = __webpack_require__(219);

/* harmony default export */ var docsvue_type_script_lang_js_ = ({
  data: function data() {
    return {
      drawer: true
    };
  },
  components: {
    DocsMenu: DocsMenu["a" /* default */]
  },
  asyncData: function asyncData() {
    var paths, imports, _loop, index, mdMeta, _ret, categorySet, categories;

    return regeneratorRuntime.async(function asyncData$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            _context2.next = 2;
            return regeneratorRuntime.awrap(fetch("/services/docs/markdown.csv").then(function (res) {
              return res.text();
            }).then(function (body) {
              return body.split("\n");
            }));

          case 2:
            paths = _context2.sent;
            imports = [];

            _loop = function _loop(index) {
              var key, _key$match, _key$match2, name, detailName, rawMD;

              return regeneratorRuntime.async(function _loop$(_context) {
                while (1) {
                  switch (_context.prev = _context.next) {
                    case 0:
                      key = paths[index];

                      if (!(key == null || key == "")) {
                        _context.next = 3;
                        break;
                      }

                      return _context.abrupt("return", "continue");

                    case 3:
                      _key$match = key.match(/(.+)\.md$/), _key$match2 = Object(slicedToArray["a" /* default */])(_key$match, 2), name = _key$match2[1];
                      detailName = key.split("/").filter(function (x) {
                        return x.includes(".md");
                      })[0];
                      detailName = detailName.substr(0, detailName.length - 3); //const mdMeta = resolve(key);

                      _context.next = 8;
                      return regeneratorRuntime.awrap(fetch("/services/docs/markdown/" + key).then(function (res) {
                        return res.text();
                      }).then(function (body) {
                        return rawMD = rawMD + body;
                      }));

                    case 8:
                      rawMD = _context.sent;
                      mdMeta = fm(rawMD);

                      if (mdMeta.attributes == null || mdMeta.attributes.title == null) {
                        mdMeta.attributes.title = detailName;
                      }

                      mdMeta.categories = key.split("/").filter(function (x) {
                        return !x.includes(".");
                      });
                      mdMeta.filename = "/docs/" + encodeURIComponent(name);
                      console.log(key);
                      imports.push(mdMeta);

                    case 15:
                    case "end":
                      return _context.stop();
                  }
                }
              });
            };

            _context2.t0 = regeneratorRuntime.keys(paths);

          case 6:
            if ((_context2.t1 = _context2.t0()).done) {
              _context2.next = 15;
              break;
            }

            index = _context2.t1.value;
            _context2.next = 10;
            return regeneratorRuntime.awrap(_loop(index));

          case 10:
            _ret = _context2.sent;

            if (!(_ret === "continue")) {
              _context2.next = 13;
              break;
            }

            return _context2.abrupt("continue", 6);

          case 13:
            _context2.next = 6;
            break;

          case 15:
            categorySet = new Set();
            imports.forEach(function (x) {
              categorySet.add(x.categories.toString());
            });
            categories = Array.from(categorySet).map(function (category) {
              var docs = imports.filter(function (x) {
                return x.categories.toString() == category;
              });
              return {
                category: category,
                docs: docs
              };
            });
            return _context2.abrupt("return", {
              categories: categories
            });

          case 19:
          case "end":
            return _context2.stop();
        }
      }
    });
  }
});
// CONCATENATED MODULE: ./pages/docs/index.vue?vue&type=script&lang=js&
 /* harmony default export */ var pages_docsvue_type_script_lang_js_ = (docsvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./node_modules/vue-loader/lib/runtime/componentNormalizer.js
var componentNormalizer = __webpack_require__(38);

// EXTERNAL MODULE: ./node_modules/vuetify-loader/lib/runtime/installComponents.js
var installComponents = __webpack_require__(188);
var installComponents_default = /*#__PURE__*/__webpack_require__.n(installComponents);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VApp/VApp.js
var VApp = __webpack_require__(374);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VAppBar/VAppBar.js + 2 modules
var VAppBar = __webpack_require__(383);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VAppBar/VAppBarNavIcon.js + 4 modules
var VAppBarNavIcon = __webpack_require__(381);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VContent/VContent.js
var VContent = __webpack_require__(375);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VFooter/VFooter.js
var VFooter = __webpack_require__(376);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VNavigationDrawer/VNavigationDrawer.js + 11 modules
var VNavigationDrawer = __webpack_require__(378);

// EXTERNAL MODULE: ./node_modules/vuetify/lib/components/VToolbar/index.js
var VToolbar = __webpack_require__(223);

// CONCATENATED MODULE: ./pages/docs/index.vue





/* normalize component */

var component = Object(componentNormalizer["a" /* default */])(
  pages_docsvue_type_script_lang_js_,
  render,
  staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var docs = __webpack_exports__["default"] = (component.exports);

/* vuetify-loader */








installComponents_default()(component, {VApp: VApp["a" /* default */],VAppBar: VAppBar["a" /* default */],VAppBarNavIcon: VAppBarNavIcon["a" /* default */],VContent: VContent["a" /* default */],VFooter: VFooter["a" /* default */],VNavigationDrawer: VNavigationDrawer["a" /* default */],VToolbarTitle: VToolbar["a" /* VToolbarTitle */]})


/***/ })

}]);