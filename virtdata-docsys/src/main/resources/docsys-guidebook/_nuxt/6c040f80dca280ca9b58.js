(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[1],{

/***/ 1:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export empty */
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "i", function() { return globalHandleError; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "j", function() { return interopDefault; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return applyAsyncData; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "o", function() { return sanitizeComponent; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "e", function() { return getMatchedComponents; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "f", function() { return getMatchedComponentsInstances; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "c", function() { return flatMapComponents; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "n", function() { return resolveRouteComponents; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "h", function() { return getRouteData; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "p", function() { return setContext; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "k", function() { return middlewareSeries; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "m", function() { return promisify; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "d", function() { return getLocation; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return compile; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "g", function() { return getQueryDiff; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "l", function() { return normalizeError; });
/* harmony import */ var core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(33);
/* harmony import */ var core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(21);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var core_js_modules_es6_regexp_split__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(55);
/* harmony import */ var core_js_modules_es6_regexp_split__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_split__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(42);
/* harmony import */ var core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var core_js_modules_es6_string_repeat__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(70);
/* harmony import */ var core_js_modules_es6_string_repeat__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_repeat__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var core_js_modules_es6_regexp_to_string__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(56);
/* harmony import */ var core_js_modules_es6_regexp_to_string__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_to_string__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var core_js_modules_es6_date_to_string__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(57);
/* harmony import */ var core_js_modules_es6_date_to_string__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_date_to_string__WEBPACK_IMPORTED_MODULE_6__);
/* harmony import */ var core_js_modules_es6_regexp_constructor__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(149);
/* harmony import */ var core_js_modules_es6_regexp_constructor__WEBPACK_IMPORTED_MODULE_7___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_constructor__WEBPACK_IMPORTED_MODULE_7__);
/* harmony import */ var core_js_modules_es6_regexp_search__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(151);
/* harmony import */ var core_js_modules_es6_regexp_search__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_search__WEBPACK_IMPORTED_MODULE_8__);
/* harmony import */ var _babel_runtime_helpers_esm_slicedToArray__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(15);
/* harmony import */ var core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(39);
/* harmony import */ var core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_10___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_10__);
/* harmony import */ var _babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(16);
/* harmony import */ var regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(59);
/* harmony import */ var regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_12___default = /*#__PURE__*/__webpack_require__.n(regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_12__);
/* harmony import */ var core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(58);
/* harmony import */ var core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_13___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_13__);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(13);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_14___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_14__);
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(7);
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_15___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_15__);
/* harmony import */ var core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(22);
/* harmony import */ var core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_16___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_16__);
/* harmony import */ var core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_17__ = __webpack_require__(26);
/* harmony import */ var core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_17___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_17__);
/* harmony import */ var _babel_runtime_helpers_esm_defineProperty__WEBPACK_IMPORTED_MODULE_18__ = __webpack_require__(8);
/* harmony import */ var vue__WEBPACK_IMPORTED_MODULE_19__ = __webpack_require__(0);




















function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { Object(_babel_runtime_helpers_esm_defineProperty__WEBPACK_IMPORTED_MODULE_18__[/* default */ "a"])(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }

 // window.{{globals.loadedCallback}} hook
// Useful for jsdom testing or plugins (https://github.com/tmpvar/jsdom#dealing-with-asynchronous-script-loading)

if (true) {
  window.onNuxtReadyCbs = [];

  window.onNuxtReady = function (cb) {
    window.onNuxtReadyCbs.push(cb);
  };
}

function empty() {}
function globalHandleError(error) {
  if (vue__WEBPACK_IMPORTED_MODULE_19__[/* default */ "a"].config.errorHandler) {
    vue__WEBPACK_IMPORTED_MODULE_19__[/* default */ "a"].config.errorHandler(error);
  }
}
function interopDefault(promise) {
  return promise.then(function (m) {
    return m.default || m;
  });
}
function applyAsyncData(Component, asyncData) {
  if ( // For SSR, we once all this function without second param to just apply asyncData
  // Prevent doing this for each SSR request
  !asyncData && Component.options.__hasNuxtData) {
    return;
  }

  var ComponentData = Component.options._originDataFn || Component.options.data || function () {
    return {};
  };

  Component.options._originDataFn = ComponentData;

  Component.options.data = function () {
    var data = ComponentData.call(this, this);

    if (this.$ssrContext) {
      asyncData = this.$ssrContext.asyncData[Component.cid];
    }

    return _objectSpread({}, data, {}, asyncData);
  };

  Component.options.__hasNuxtData = true;

  if (Component._Ctor && Component._Ctor.options) {
    Component._Ctor.options.data = Component.options.data;
  }
}
function sanitizeComponent(Component) {
  // If Component already sanitized
  if (Component.options && Component._Ctor === Component) {
    return Component;
  }

  if (!Component.options) {
    Component = vue__WEBPACK_IMPORTED_MODULE_19__[/* default */ "a"].extend(Component); // fix issue #6

    Component._Ctor = Component;
  } else {
    Component._Ctor = Component;
    Component.extendOptions = Component.options;
  } // For debugging purpose


  if (!Component.options.name && Component.options.__file) {
    Component.options.name = Component.options.__file;
  }

  return Component;
}
function getMatchedComponents(route) {
  var matches = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
  var prop = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'components';
  return Array.prototype.concat.apply([], route.matched.map(function (m, index) {
    return Object.keys(m[prop]).map(function (key) {
      matches && matches.push(index);
      return m[prop][key];
    });
  }));
}
function getMatchedComponentsInstances(route) {
  var matches = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
  return getMatchedComponents(route, matches, 'instances');
}
function flatMapComponents(route, fn) {
  return Array.prototype.concat.apply([], route.matched.map(function (m, index) {
    return Object.keys(m.components).reduce(function (promises, key) {
      if (m.components[key]) {
        promises.push(fn(m.components[key], m.instances[key], m, key, index));
      } else {
        delete m.components[key];
      }

      return promises;
    }, []);
  }));
}
function resolveRouteComponents(route, fn) {
  return Promise.all(flatMapComponents(route, function _callee(Component, instance, match, key) {
    return regeneratorRuntime.async(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            if (!(typeof Component === 'function' && !Component.options)) {
              _context.next = 4;
              break;
            }

            _context.next = 3;
            return regeneratorRuntime.awrap(Component());

          case 3:
            Component = _context.sent;

          case 4:
            match.components[key] = Component = sanitizeComponent(Component);
            return _context.abrupt("return", typeof fn === 'function' ? fn(Component, instance, match, key) : Component);

          case 6:
          case "end":
            return _context.stop();
        }
      }
    });
  }));
}
function getRouteData(route) {
  return regeneratorRuntime.async(function getRouteData$(_context2) {
    while (1) {
      switch (_context2.prev = _context2.next) {
        case 0:
          if (route) {
            _context2.next = 2;
            break;
          }

          return _context2.abrupt("return");

        case 2:
          _context2.next = 4;
          return regeneratorRuntime.awrap(resolveRouteComponents(route));

        case 4:
          return _context2.abrupt("return", _objectSpread({}, route, {
            meta: getMatchedComponents(route).map(function (Component, index) {
              return _objectSpread({}, Component.options.meta, {}, (route.matched[index] || {}).meta);
            })
          }));

        case 5:
        case "end":
          return _context2.stop();
      }
    }
  });
}
function setContext(app, context) {
  var _ref, _ref2, currentRouteData, fromRouteData;

  return regeneratorRuntime.async(function setContext$(_context3) {
    while (1) {
      switch (_context3.prev = _context3.next) {
        case 0:
          // If context not defined, create it
          if (!app.context) {
            app.context = {
              isStatic: true,
              isDev: false,
              isHMR: false,
              app: app,
              payload: context.payload,
              error: context.error,
              base: '/',
              env: {}
            }; // Only set once

            if (context.req) {
              app.context.req = context.req;
            }

            if (context.res) {
              app.context.res = context.res;
            }

            if (context.ssrContext) {
              app.context.ssrContext = context.ssrContext;
            }

            app.context.redirect = function (status, path, query) {
              if (!status) {
                return;
              }

              app.context._redirected = true; // if only 1 or 2 arguments: redirect('/') or redirect('/', { foo: 'bar' })

              var pathType = Object(_babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_11__[/* default */ "a"])(path);

              if (typeof status !== 'number' && (pathType === 'undefined' || pathType === 'object')) {
                query = path || {};
                path = status;
                pathType = Object(_babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_11__[/* default */ "a"])(path);
                status = 302;
              }

              if (pathType === 'object') {
                path = app.router.resolve(path).route.fullPath;
              } // "/absolute/route", "./relative/route" or "../relative/route"


              if (/(^[.]{1,2}\/)|(^\/(?!\/))/.test(path)) {
                app.context.next({
                  path: path,
                  query: query,
                  status: status
                });
              } else {
                path = formatUrl(path, query);

                if (false) {}

                if (true) {
                  // https://developer.mozilla.org/en-US/docs/Web/API/Location/replace
                  window.location.replace(path); // Throw a redirect error

                  throw new Error('ERR_REDIRECT');
                }
              }
            };

            if (false) {}

            if (true) {
              app.context.nuxtState = window.__NUXT__;
            }
          } // Dynamic keys


          _context3.next = 3;
          return regeneratorRuntime.awrap(Promise.all([getRouteData(context.route), getRouteData(context.from)]));

        case 3:
          _ref = _context3.sent;
          _ref2 = Object(_babel_runtime_helpers_esm_slicedToArray__WEBPACK_IMPORTED_MODULE_9__[/* default */ "a"])(_ref, 2);
          currentRouteData = _ref2[0];
          fromRouteData = _ref2[1];

          if (context.route) {
            app.context.route = currentRouteData;
          }

          if (context.from) {
            app.context.from = fromRouteData;
          }

          app.context.next = context.next;
          app.context._redirected = false;
          app.context._errored = false;
          app.context.isHMR = false;
          app.context.params = app.context.route.params || {};
          app.context.query = app.context.route.query || {};

        case 15:
        case "end":
          return _context3.stop();
      }
    }
  });
}
function middlewareSeries(promises, appContext) {
  if (!promises.length || appContext._redirected || appContext._errored) {
    return Promise.resolve();
  }

  return promisify(promises[0], appContext).then(function () {
    return middlewareSeries(promises.slice(1), appContext);
  });
}
function promisify(fn, context) {
  var promise;

  if (fn.length === 2) {
    // fn(context, callback)
    promise = new Promise(function (resolve) {
      fn(context, function (err, data) {
        if (err) {
          context.error(err);
        }

        data = data || {};
        resolve(data);
      });
    });
  } else {
    promise = fn(context);
  }

  if (promise && promise instanceof Promise && typeof promise.then === 'function') {
    return promise;
  }

  return Promise.resolve(promise);
} // Imported from vue-router

function getLocation(base, mode) {
  var path = decodeURI(window.location.pathname);

  if (mode === 'hash') {
    return window.location.hash.replace(/^#\//, '');
  }

  if (base && path.indexOf(base) === 0) {
    path = path.slice(base.length);
  }

  return (path || '/') + window.location.search + window.location.hash;
} // Imported from path-to-regexp

/**
 * Compile a string to a template function for the path.
 *
 * @param  {string}             str
 * @param  {Object=}            options
 * @return {!function(Object=, Object=)}
 */

function compile(str, options) {
  return tokensToFunction(parse(str, options), options);
}
function getQueryDiff(toQuery, fromQuery) {
  var diff = {};

  var queries = _objectSpread({}, toQuery, {}, fromQuery);

  for (var k in queries) {
    if (String(toQuery[k]) !== String(fromQuery[k])) {
      diff[k] = true;
    }
  }

  return diff;
}
function normalizeError(err) {
  var message;

  if (!(err.message || typeof err === 'string')) {
    try {
      message = JSON.stringify(err, null, 2);
    } catch (e) {
      message = "[".concat(err.constructor.name, "]");
    }
  } else {
    message = err.message || err;
  }

  return _objectSpread({}, err, {
    message: message,
    statusCode: err.statusCode || err.status || err.response && err.response.status || 500
  });
}
/**
 * The main path matching regexp utility.
 *
 * @type {RegExp}
 */

var PATH_REGEXP = new RegExp([// Match escaped characters that would otherwise appear in future matches.
// This allows the user to escape special characters that won't transform.
'(\\\\.)', // Match Express-style parameters and un-named parameters with a prefix
// and optional suffixes. Matches appear as:
//
// "/:test(\\d+)?" => ["/", "test", "\d+", undefined, "?", undefined]
// "/route(\\d+)"  => [undefined, undefined, undefined, "\d+", undefined, undefined]
// "/*"            => ["/", undefined, undefined, undefined, undefined, "*"]
'([\\/.])?(?:(?:\\:(\\w+)(?:\\(((?:\\\\.|[^\\\\()])+)\\))?|\\(((?:\\\\.|[^\\\\()])+)\\))([+*?])?|(\\*))'].join('|'), 'g');
/**
 * Parse a string for the raw tokens.
 *
 * @param  {string}  str
 * @param  {Object=} options
 * @return {!Array}
 */

function parse(str, options) {
  var tokens = [];
  var key = 0;
  var index = 0;
  var path = '';
  var defaultDelimiter = options && options.delimiter || '/';
  var res;

  while ((res = PATH_REGEXP.exec(str)) != null) {
    var m = res[0];
    var escaped = res[1];
    var offset = res.index;
    path += str.slice(index, offset);
    index = offset + m.length; // Ignore already escaped sequences.

    if (escaped) {
      path += escaped[1];
      continue;
    }

    var next = str[index];
    var prefix = res[2];
    var name = res[3];
    var capture = res[4];
    var group = res[5];
    var modifier = res[6];
    var asterisk = res[7]; // Push the current path onto the tokens.

    if (path) {
      tokens.push(path);
      path = '';
    }

    var partial = prefix != null && next != null && next !== prefix;
    var repeat = modifier === '+' || modifier === '*';
    var optional = modifier === '?' || modifier === '*';
    var delimiter = res[2] || defaultDelimiter;
    var pattern = capture || group;
    tokens.push({
      name: name || key++,
      prefix: prefix || '',
      delimiter: delimiter,
      optional: optional,
      repeat: repeat,
      partial: partial,
      asterisk: Boolean(asterisk),
      pattern: pattern ? escapeGroup(pattern) : asterisk ? '.*' : '[^' + escapeString(delimiter) + ']+?'
    });
  } // Match any characters still remaining.


  if (index < str.length) {
    path += str.substr(index);
  } // If the path exists, push it onto the end.


  if (path) {
    tokens.push(path);
  }

  return tokens;
}
/**
 * Prettier encoding of URI path segments.
 *
 * @param  {string}
 * @return {string}
 */


function encodeURIComponentPretty(str, slashAllowed) {
  var re = slashAllowed ? /[?#]/g : /[/?#]/g;
  return encodeURI(str).replace(re, function (c) {
    return '%' + c.charCodeAt(0).toString(16).toUpperCase();
  });
}
/**
 * Encode the asterisk parameter. Similar to `pretty`, but allows slashes.
 *
 * @param  {string}
 * @return {string}
 */


function encodeAsterisk(str) {
  return encodeURIComponentPretty(str, true);
}
/**
 * Escape a regular expression string.
 *
 * @param  {string} str
 * @return {string}
 */


function escapeString(str) {
  return str.replace(/([.+*?=^!:${}()[\]|/\\])/g, '\\$1');
}
/**
 * Escape the capturing group by escaping special characters and meaning.
 *
 * @param  {string} group
 * @return {string}
 */


function escapeGroup(group) {
  return group.replace(/([=!:$/()])/g, '\\$1');
}
/**
 * Expose a method for transforming tokens into the path function.
 */


function tokensToFunction(tokens, options) {
  // Compile all the tokens into regexps.
  var matches = new Array(tokens.length); // Compile all the patterns before compilation.

  for (var i = 0; i < tokens.length; i++) {
    if (Object(_babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_11__[/* default */ "a"])(tokens[i]) === 'object') {
      matches[i] = new RegExp('^(?:' + tokens[i].pattern + ')$', flags(options));
    }
  }

  return function (obj, opts) {
    var path = '';
    var data = obj || {};
    var options = opts || {};
    var encode = options.pretty ? encodeURIComponentPretty : encodeURIComponent;

    for (var _i = 0; _i < tokens.length; _i++) {
      var token = tokens[_i];

      if (typeof token === 'string') {
        path += token;
        continue;
      }

      var value = data[token.name || 'pathMatch'];
      var segment = void 0;

      if (value == null) {
        if (token.optional) {
          // Prepend partial segment prefixes.
          if (token.partial) {
            path += token.prefix;
          }

          continue;
        } else {
          throw new TypeError('Expected "' + token.name + '" to be defined');
        }
      }

      if (Array.isArray(value)) {
        if (!token.repeat) {
          throw new TypeError('Expected "' + token.name + '" to not repeat, but received `' + JSON.stringify(value) + '`');
        }

        if (value.length === 0) {
          if (token.optional) {
            continue;
          } else {
            throw new TypeError('Expected "' + token.name + '" to not be empty');
          }
        }

        for (var j = 0; j < value.length; j++) {
          segment = encode(value[j]);

          if (!matches[_i].test(segment)) {
            throw new TypeError('Expected all "' + token.name + '" to match "' + token.pattern + '", but received `' + JSON.stringify(segment) + '`');
          }

          path += (j === 0 ? token.prefix : token.delimiter) + segment;
        }

        continue;
      }

      segment = token.asterisk ? encodeAsterisk(value) : encode(value);

      if (!matches[_i].test(segment)) {
        throw new TypeError('Expected "' + token.name + '" to match "' + token.pattern + '", but received "' + segment + '"');
      }

      path += token.prefix + segment;
    }

    return path;
  };
}
/**
 * Get the flags for a regexp from the options.
 *
 * @param  {Object} options
 * @return {string}
 */


function flags(options) {
  return options && options.sensitive ? '' : 'i';
}
/**
 * Format given url, append query to url query string
 *
 * @param  {string} url
 * @param  {string} query
 * @return {string}
 */


function formatUrl(url, query) {
  var protocol;
  var index = url.indexOf('://');

  if (index !== -1) {
    protocol = url.substring(0, index);
    url = url.substring(index + 3);
  } else if (url.startsWith('//')) {
    url = url.substring(2);
  }

  var parts = url.split('/');
  var result = (protocol ? protocol + '://' : '//') + parts.shift();
  var path = parts.filter(Boolean).join('/');
  var hash;
  parts = path.split('#');

  if (parts.length === 2) {
    var _parts = parts;

    var _parts2 = Object(_babel_runtime_helpers_esm_slicedToArray__WEBPACK_IMPORTED_MODULE_9__[/* default */ "a"])(_parts, 2);

    path = _parts2[0];
    hash = _parts2[1];
  }

  result += path ? '/' + path : '';

  if (query && JSON.stringify(query) !== '{}') {
    result += (url.split('?').length === 2 ? '&' : '?') + formatQuery(query);
  }

  result += hash ? '#' + hash : '';
  return result;
}
/**
 * Transform data object to query string
 *
 * @param  {object} query
 * @return {string}
 */


function formatQuery(query) {
  return Object.keys(query).sort().map(function (key) {
    var val = query[key];

    if (val == null) {
      return '';
    }

    if (Array.isArray(val)) {
      return val.slice().map(function (val2) {
        return [key, '=', val2].join('');
      }).join('&');
    }

    return key + '=' + val;
  }).filter(Boolean).join('&');
}

/***/ }),

/***/ 128:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(129);


/***/ }),

/***/ 129:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* WEBPACK VAR INJECTION */(function(global) {/* harmony import */ var _babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(16);
/* harmony import */ var core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(58);
/* harmony import */ var core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_iterator__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(75);
/* harmony import */ var core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(21);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var core_js_modules_es6_regexp_match__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(90);
/* harmony import */ var core_js_modules_es6_regexp_match__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_regexp_match__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(59);
/* harmony import */ var regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(regenerator_runtime_runtime__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(53);
/* harmony import */ var core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_6__);
/* harmony import */ var core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(54);
/* harmony import */ var core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_7___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_7__);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(13);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_8__);
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(7);
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_9___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_9__);
/* harmony import */ var core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(22);
/* harmony import */ var core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_10___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_10__);
/* harmony import */ var core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(26);
/* harmony import */ var core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_11___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_function_name__WEBPACK_IMPORTED_MODULE_11__);
/* harmony import */ var core_js_modules_es6_array_iterator__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(106);
/* harmony import */ var core_js_modules_es6_array_iterator__WEBPACK_IMPORTED_MODULE_12___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_array_iterator__WEBPACK_IMPORTED_MODULE_12__);
/* harmony import */ var core_js_modules_es6_promise__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(138);
/* harmony import */ var core_js_modules_es6_promise__WEBPACK_IMPORTED_MODULE_13___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_promise__WEBPACK_IMPORTED_MODULE_13__);
/* harmony import */ var core_js_modules_es6_object_assign__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(143);
/* harmony import */ var core_js_modules_es6_object_assign__WEBPACK_IMPORTED_MODULE_14___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_assign__WEBPACK_IMPORTED_MODULE_14__);
/* harmony import */ var core_js_modules_es7_promise_finally__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(145);
/* harmony import */ var core_js_modules_es7_promise_finally__WEBPACK_IMPORTED_MODULE_15___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_promise_finally__WEBPACK_IMPORTED_MODULE_15__);
/* harmony import */ var vue__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(0);
/* harmony import */ var unfetch__WEBPACK_IMPORTED_MODULE_17__ = __webpack_require__(115);
/* harmony import */ var _middleware_js__WEBPACK_IMPORTED_MODULE_18__ = __webpack_require__(87);
/* harmony import */ var _utils_js__WEBPACK_IMPORTED_MODULE_19__ = __webpack_require__(1);
/* harmony import */ var _index_js__WEBPACK_IMPORTED_MODULE_20__ = __webpack_require__(25);
/* harmony import */ var _components_nuxt_link_client_js__WEBPACK_IMPORTED_MODULE_21__ = __webpack_require__(74);





















 // should be included after ./index.js
// Component: <NuxtLink>

vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].component(_components_nuxt_link_client_js__WEBPACK_IMPORTED_MODULE_21__[/* default */ "a"].name, _components_nuxt_link_client_js__WEBPACK_IMPORTED_MODULE_21__[/* default */ "a"]);
vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].component('NLink', _components_nuxt_link_client_js__WEBPACK_IMPORTED_MODULE_21__[/* default */ "a"]);

if (!global.fetch) {
  global.fetch = unfetch__WEBPACK_IMPORTED_MODULE_17__[/* default */ "a"];
} // Global shared references


var _lastPaths = [];
var app;
var router; // Try to rehydrate SSR data from window

var NUXT = window.__NUXT__ || {};
Object.assign(vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].config, {
  "silent": true,
  "performance": false
});
var errorHandler = vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].config.errorHandler || console.error; // Create and mount App

Object(_index_js__WEBPACK_IMPORTED_MODULE_20__[/* createApp */ "b"])().then(mountApp).catch(errorHandler);

function componentOption(component, key) {
  if (!component || !component.options || !component.options[key]) {
    return {};
  }

  var option = component.options[key];

  if (typeof option === 'function') {
    for (var _len = arguments.length, args = new Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
      args[_key - 2] = arguments[_key];
    }

    return option.apply(void 0, args);
  }

  return option;
}

function mapTransitions(Components, to, from) {
  var componentTransitions = function componentTransitions(component) {
    var transition = componentOption(component, 'transition', to, from) || {};
    return typeof transition === 'string' ? {
      name: transition
    } : transition;
  };

  return Components.map(function (Component) {
    // Clone original object to prevent overrides
    var transitions = Object.assign({}, componentTransitions(Component)); // Combine transitions & prefer `leave` transitions of 'from' route

    if (from && from.matched.length && from.matched[0].components.default) {
      var fromTransitions = componentTransitions(from.matched[0].components.default);
      Object.keys(fromTransitions).filter(function (key) {
        return fromTransitions[key] && key.toLowerCase().includes('leave');
      }).forEach(function (key) {
        transitions[key] = fromTransitions[key];
      });
    }

    return transitions;
  });
}

function loadAsyncComponents(to, from, next) {
  var _this = this;

  var Components, startLoader, err, statusCode, message;
  return regeneratorRuntime.async(function loadAsyncComponents$(_context) {
    while (1) {
      switch (_context.prev = _context.next) {
        case 0:
          // Check if route path changed (this._pathChanged), only if the page is not an error (for validate())
          this._pathChanged = Boolean(app.nuxt.err) || from.path !== to.path;
          this._queryChanged = JSON.stringify(to.query) !== JSON.stringify(from.query);
          this._diffQuery = this._queryChanged ? Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getQueryDiff */ "g"])(to.query, from.query) : [];

          if (this._pathChanged && this.$loading.start && !this.$loading.manual) {
            this.$loading.start();
          }

          _context.prev = 4;

          if (!(!this._pathChanged && this._queryChanged)) {
            _context.next = 11;
            break;
          }

          _context.next = 8;
          return regeneratorRuntime.awrap(Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* resolveRouteComponents */ "n"])(to, function (Component, instance) {
            return {
              Component: Component,
              instance: instance
            };
          }));

        case 8:
          Components = _context.sent;
          // Add a marker on each component that it needs to refresh or not
          startLoader = Components.some(function (_ref) {
            var Component = _ref.Component,
                instance = _ref.instance;
            var watchQuery = Component.options.watchQuery;

            if (watchQuery === true) {
              return true;
            }

            if (Array.isArray(watchQuery)) {
              return watchQuery.some(function (key) {
                return _this._diffQuery[key];
              });
            }

            if (typeof watchQuery === 'function') {
              return watchQuery.apply(instance, [to.query, from.query]);
            }

            return false;
          });

          if (startLoader && this.$loading.start && !this.$loading.manual) {
            this.$loading.start();
          }

        case 11:
          // Call next()
          next();
          _context.next = 25;
          break;

        case 14:
          _context.prev = 14;
          _context.t0 = _context["catch"](4);
          err = _context.t0 || {};
          statusCode = err.statusCode || err.status || err.response && err.response.status || 500;
          message = err.message || ''; // Handle chunk loading errors
          // This may be due to a new deployment or a network problem

          if (!/^Loading( CSS)? chunk (\d)+ failed\./.test(message)) {
            _context.next = 22;
            break;
          }

          window.location.reload(true
          /* skip cache */
          );
          return _context.abrupt("return");

        case 22:
          this.error({
            statusCode: statusCode,
            message: message
          });
          this.$nuxt.$emit('routeChanged', to, from, err);
          next();

        case 25:
        case "end":
          return _context.stop();
      }
    }
  }, null, this, [[4, 14]]);
}

function applySSRData(Component, ssrData) {
  if (NUXT.serverRendered && ssrData) {
    Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* applyAsyncData */ "a"])(Component, ssrData);
  }

  Component._Ctor = Component;
  return Component;
} // Get matched components


function resolveComponents(router) {
  var path = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getLocation */ "d"])(router.options.base, router.options.mode);
  return Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* flatMapComponents */ "c"])(router.match(path), function _callee(Component, _, match, key, index) {
    var _Component;

    return regeneratorRuntime.async(function _callee$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            if (!(typeof Component === 'function' && !Component.options)) {
              _context2.next = 4;
              break;
            }

            _context2.next = 3;
            return regeneratorRuntime.awrap(Component());

          case 3:
            Component = _context2.sent;

          case 4:
            // Sanitize it and save it
            _Component = applySSRData(Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* sanitizeComponent */ "o"])(Component), NUXT.data ? NUXT.data[index] : null);
            match.components[key] = _Component;
            return _context2.abrupt("return", _Component);

          case 7:
          case "end":
            return _context2.stop();
        }
      }
    });
  });
}

function callMiddleware(Components, context, layout) {
  var _this2 = this;

  var midd = [];
  var unknownMiddleware = false; // If layout is undefined, only call global middleware

  if (typeof layout !== 'undefined') {
    midd = []; // Exclude global middleware if layout defined (already called before)

    layout = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* sanitizeComponent */ "o"])(layout);

    if (layout.options.middleware) {
      midd = midd.concat(layout.options.middleware);
    }

    Components.forEach(function (Component) {
      if (Component.options.middleware) {
        midd = midd.concat(Component.options.middleware);
      }
    });
  }

  midd = midd.map(function (name) {
    if (typeof name === 'function') {
      return name;
    }

    if (typeof _middleware_js__WEBPACK_IMPORTED_MODULE_18__[/* default */ "a"][name] !== 'function') {
      unknownMiddleware = true;

      _this2.error({
        statusCode: 500,
        message: 'Unknown middleware ' + name
      });
    }

    return _middleware_js__WEBPACK_IMPORTED_MODULE_18__[/* default */ "a"][name];
  });

  if (unknownMiddleware) {
    return;
  }

  return Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* middlewareSeries */ "k"])(midd, context);
}

function render(to, from, next) {
  var _this3 = this;

  var fromMatches, nextCalled, _next, matches, Components, errorLayout, layout, _layout, isValid, _iteratorNormalCompletion, _didIteratorError, _iteratorError, _iterator, _step, Component, instances, error, _layout2;

  return regeneratorRuntime.async(function render$(_context3) {
    while (1) {
      switch (_context3.prev = _context3.next) {
        case 0:
          if (!(this._pathChanged === false && this._queryChanged === false)) {
            _context3.next = 2;
            break;
          }

          return _context3.abrupt("return", next());

        case 2:
          // Handle first render on SPA mode
          if (to === from) {
            _lastPaths = [];
          } else {
            fromMatches = [];
            _lastPaths = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getMatchedComponents */ "e"])(from, fromMatches).map(function (Component, i) {
              return Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* compile */ "b"])(from.matched[fromMatches[i]].path)(from.params);
            });
          } // nextCalled is true when redirected


          nextCalled = false;

          _next = function _next(path) {
            if (from.path === path.path && _this3.$loading.finish) {
              _this3.$loading.finish();
            }

            if (from.path !== path.path && _this3.$loading.pause) {
              _this3.$loading.pause();
            }

            if (nextCalled) {
              return;
            }

            nextCalled = true;
            next(path);
          }; // Update context


          _context3.next = 7;
          return regeneratorRuntime.awrap(Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* setContext */ "p"])(app, {
            route: to,
            from: from,
            next: _next.bind(this)
          }));

        case 7:
          this._dateLastError = app.nuxt.dateErr;
          this._hadError = Boolean(app.nuxt.err); // Get route's matched components

          matches = [];
          Components = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getMatchedComponents */ "e"])(to, matches); // If no Components matched, generate 404

          if (Components.length) {
            _context3.next = 26;
            break;
          }

          _context3.next = 14;
          return regeneratorRuntime.awrap(callMiddleware.call(this, Components, app.context));

        case 14:
          if (!nextCalled) {
            _context3.next = 16;
            break;
          }

          return _context3.abrupt("return");

        case 16:
          // Load layout for error page
          errorLayout = (_index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"].options || _index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"]).layout;
          _context3.next = 19;
          return regeneratorRuntime.awrap(this.loadLayout(typeof errorLayout === 'function' ? errorLayout.call(_index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"], app.context) : errorLayout));

        case 19:
          layout = _context3.sent;
          _context3.next = 22;
          return regeneratorRuntime.awrap(callMiddleware.call(this, Components, app.context, layout));

        case 22:
          if (!nextCalled) {
            _context3.next = 24;
            break;
          }

          return _context3.abrupt("return");

        case 24:
          // Show error page
          app.context.error({
            statusCode: 404,
            message: 'This page could not be found'
          });
          return _context3.abrupt("return", next());

        case 26:
          // Update ._data and other properties if hot reloaded
          Components.forEach(function (Component) {
            if (Component._Ctor && Component._Ctor.options) {
              Component.options.asyncData = Component._Ctor.options.asyncData;
              Component.options.fetch = Component._Ctor.options.fetch;
            }
          }); // Apply transitions

          this.setTransitions(mapTransitions(Components, to, from));
          _context3.prev = 28;
          _context3.next = 31;
          return regeneratorRuntime.awrap(callMiddleware.call(this, Components, app.context));

        case 31:
          if (!nextCalled) {
            _context3.next = 33;
            break;
          }

          return _context3.abrupt("return");

        case 33:
          if (!app.context._errored) {
            _context3.next = 35;
            break;
          }

          return _context3.abrupt("return", next());

        case 35:
          // Set layout
          _layout = Components[0].options.layout;

          if (typeof _layout === 'function') {
            _layout = _layout(app.context);
          }

          _context3.next = 39;
          return regeneratorRuntime.awrap(this.loadLayout(_layout));

        case 39:
          _layout = _context3.sent;
          _context3.next = 42;
          return regeneratorRuntime.awrap(callMiddleware.call(this, Components, app.context, _layout));

        case 42:
          if (!nextCalled) {
            _context3.next = 44;
            break;
          }

          return _context3.abrupt("return");

        case 44:
          if (!app.context._errored) {
            _context3.next = 46;
            break;
          }

          return _context3.abrupt("return", next());

        case 46:
          // Call .validate()
          isValid = true;
          _context3.prev = 47;
          _iteratorNormalCompletion = true;
          _didIteratorError = false;
          _iteratorError = undefined;
          _context3.prev = 51;
          _iterator = Components[Symbol.iterator]();

        case 53:
          if (_iteratorNormalCompletion = (_step = _iterator.next()).done) {
            _context3.next = 65;
            break;
          }

          Component = _step.value;

          if (!(typeof Component.options.validate !== 'function')) {
            _context3.next = 57;
            break;
          }

          return _context3.abrupt("continue", 62);

        case 57:
          _context3.next = 59;
          return regeneratorRuntime.awrap(Component.options.validate(app.context));

        case 59:
          isValid = _context3.sent;

          if (isValid) {
            _context3.next = 62;
            break;
          }

          return _context3.abrupt("break", 65);

        case 62:
          _iteratorNormalCompletion = true;
          _context3.next = 53;
          break;

        case 65:
          _context3.next = 71;
          break;

        case 67:
          _context3.prev = 67;
          _context3.t0 = _context3["catch"](51);
          _didIteratorError = true;
          _iteratorError = _context3.t0;

        case 71:
          _context3.prev = 71;
          _context3.prev = 72;

          if (!_iteratorNormalCompletion && _iterator.return != null) {
            _iterator.return();
          }

        case 74:
          _context3.prev = 74;

          if (!_didIteratorError) {
            _context3.next = 77;
            break;
          }

          throw _iteratorError;

        case 77:
          return _context3.finish(74);

        case 78:
          return _context3.finish(71);

        case 79:
          _context3.next = 85;
          break;

        case 81:
          _context3.prev = 81;
          _context3.t1 = _context3["catch"](47);
          // ...If .validate() threw an error
          this.error({
            statusCode: _context3.t1.statusCode || '500',
            message: _context3.t1.message
          });
          return _context3.abrupt("return", next());

        case 85:
          if (isValid) {
            _context3.next = 88;
            break;
          }

          this.error({
            statusCode: 404,
            message: 'This page could not be found'
          });
          return _context3.abrupt("return", next());

        case 88:
          _context3.next = 90;
          return regeneratorRuntime.awrap(Promise.all(Components.map(function (Component, i) {
            // Check if only children route changed
            Component._path = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* compile */ "b"])(to.matched[matches[i]].path)(to.params);
            Component._dataRefresh = false; // Check if Component need to be refreshed (call asyncData & fetch)
            // Only if its slug has changed or is watch query changes

            if (_this3._pathChanged && _this3._queryChanged || Component._path !== _lastPaths[i]) {
              Component._dataRefresh = true;
            } else if (!_this3._pathChanged && _this3._queryChanged) {
              var watchQuery = Component.options.watchQuery;

              if (watchQuery === true) {
                Component._dataRefresh = true;
              } else if (Array.isArray(watchQuery)) {
                Component._dataRefresh = watchQuery.some(function (key) {
                  return _this3._diffQuery[key];
                });
              } else if (typeof watchQuery === 'function') {
                if (!instances) {
                  instances = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getMatchedComponentsInstances */ "f"])(to);
                }

                Component._dataRefresh = watchQuery.apply(instances[i], [to.query, from.query]);
              }
            }

            if (!_this3._hadError && _this3._isMounted && !Component._dataRefresh) {
              return;
            }

            var promises = [];
            var hasAsyncData = Component.options.asyncData && typeof Component.options.asyncData === 'function';
            var hasFetch = Boolean(Component.options.fetch);
            var loadingIncrease = hasAsyncData && hasFetch ? 30 : 45; // Call asyncData(context)

            if (hasAsyncData) {
              var promise = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* promisify */ "m"])(Component.options.asyncData, app.context).then(function (asyncDataResult) {
                Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* applyAsyncData */ "a"])(Component, asyncDataResult);

                if (_this3.$loading.increase) {
                  _this3.$loading.increase(loadingIncrease);
                }
              });
              promises.push(promise);
            } // Check disabled page loading


            _this3.$loading.manual = Component.options.loading === false; // Call fetch(context)

            if (hasFetch) {
              var p = Component.options.fetch(app.context);

              if (!p || !(p instanceof Promise) && typeof p.then !== 'function') {
                p = Promise.resolve(p);
              }

              p.then(function (fetchResult) {
                if (_this3.$loading.increase) {
                  _this3.$loading.increase(loadingIncrease);
                }
              });
              promises.push(p);
            }

            return Promise.all(promises);
          })));

        case 90:
          // If not redirected
          if (!nextCalled) {
            if (this.$loading.finish && !this.$loading.manual) {
              this.$loading.finish();
            }

            next();
          }

          _context3.next = 107;
          break;

        case 93:
          _context3.prev = 93;
          _context3.t2 = _context3["catch"](28);
          error = _context3.t2 || {};

          if (!(error.message === 'ERR_REDIRECT')) {
            _context3.next = 98;
            break;
          }

          return _context3.abrupt("return", this.$nuxt.$emit('routeChanged', to, from, error));

        case 98:
          _lastPaths = [];
          Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* globalHandleError */ "i"])(error); // Load error layout

          _layout2 = (_index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"].options || _index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"]).layout;

          if (typeof _layout2 === 'function') {
            _layout2 = _layout2(app.context);
          }

          _context3.next = 104;
          return regeneratorRuntime.awrap(this.loadLayout(_layout2));

        case 104:
          this.error(error);
          this.$nuxt.$emit('routeChanged', to, from, error);
          next();

        case 107:
        case "end":
          return _context3.stop();
      }
    }
  }, null, this, [[28, 93], [47, 81], [51, 67, 71, 79], [72,, 74, 78]]);
} // Fix components format in matched, it's due to code-splitting of vue-router


function normalizeComponents(to, ___) {
  Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* flatMapComponents */ "c"])(to, function (Component, _, match, key) {
    if (Object(_babel_runtime_helpers_esm_typeof__WEBPACK_IMPORTED_MODULE_0__[/* default */ "a"])(Component) === 'object' && !Component.options) {
      // Updated via vue-router resolveAsyncComponents()
      Component = vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].extend(Component);
      Component._Ctor = Component;
      match.components[key] = Component;
    }

    return Component;
  });
}

function showNextPage(to) {
  // Hide error component if no error
  if (this._hadError && this._dateLastError === this.$options.nuxt.dateErr) {
    this.error();
  } // Set layout


  var layout = this.$options.nuxt.err ? (_index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"].options || _index_js__WEBPACK_IMPORTED_MODULE_20__[/* NuxtError */ "a"]).layout : to.matched[0].components.default.options.layout;

  if (typeof layout === 'function') {
    layout = layout(app.context);
  }

  this.setLayout(layout);
} // When navigating on a different route but the same component is used, Vue.js
// Will not update the instance data, so we have to update $data ourselves


function fixPrepatch(to, ___) {
  var _this4 = this;

  if (this._pathChanged === false && this._queryChanged === false) {
    return;
  }

  var instances = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getMatchedComponentsInstances */ "f"])(to);
  var Components = Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* getMatchedComponents */ "e"])(to);
  vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].nextTick(function () {
    instances.forEach(function (instance, i) {
      if (!instance || instance._isDestroyed) {
        return;
      }

      if (instance.constructor._dataRefresh && Components[i] === instance.constructor && instance.$vnode.data.keepAlive !== true && typeof instance.constructor.options.data === 'function') {
        var newData = instance.constructor.options.data.call(instance);

        for (var key in newData) {
          vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].set(instance.$data, key, newData[key]);
        } // Ensure to trigger scroll event after calling scrollBehavior


        window.$nuxt.$nextTick(function () {
          window.$nuxt.$emit('triggerScroll');
        });
      }
    });
    showNextPage.call(_this4, to);
  });
}

function nuxtReady(_app) {
  window.onNuxtReadyCbs.forEach(function (cb) {
    if (typeof cb === 'function') {
      cb(_app);
    }
  }); // Special JSDOM

  if (typeof window._onNuxtLoaded === 'function') {
    window._onNuxtLoaded(_app);
  } // Add router hooks


  router.afterEach(function (to, from) {
    // Wait for fixPrepatch + $data updates
    vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].nextTick(function () {
      return _app.$nuxt.$emit('routeChanged', to, from);
    });
  });
}

function mountApp(__app) {
  var _app, mount, Components, clientFirstMount;

  return regeneratorRuntime.async(function mountApp$(_context4) {
    while (1) {
      switch (_context4.prev = _context4.next) {
        case 0:
          // Set global variables
          app = __app.app;
          router = __app.router; // Create Vue instance

          _app = new vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"](app); // Mounts Vue app to DOM element

          mount = function mount() {
            _app.$mount('#__nuxt'); // Add afterEach router hooks


            router.afterEach(normalizeComponents);
            router.afterEach(fixPrepatch.bind(_app)); // Listen for first Vue update

            vue__WEBPACK_IMPORTED_MODULE_16__[/* default */ "a"].nextTick(function () {
              // Call window.{{globals.readyCallback}} callbacks
              nuxtReady(_app);
            });
          }; // Resolve route components


          _context4.next = 6;
          return regeneratorRuntime.awrap(Promise.all(resolveComponents(router)));

        case 6:
          Components = _context4.sent;
          // Enable transitions
          _app.setTransitions = _app.$options.nuxt.setTransitions.bind(_app);

          if (Components.length) {
            _app.setTransitions(mapTransitions(Components, router.currentRoute));

            _lastPaths = router.currentRoute.matched.map(function (route) {
              return Object(_utils_js__WEBPACK_IMPORTED_MODULE_19__[/* compile */ "b"])(route.path)(router.currentRoute.params);
            });
          } // Initialize error handler


          _app.$loading = {}; // To avoid error while _app.$nuxt does not exist

          if (NUXT.error) {
            _app.error(NUXT.error);
          } // Add beforeEach router hooks


          router.beforeEach(loadAsyncComponents.bind(_app));
          router.beforeEach(render.bind(_app)); // If page already is server rendered

          if (!NUXT.serverRendered) {
            _context4.next = 16;
            break;
          }

          mount();
          return _context4.abrupt("return");

        case 16:
          // First render on client-side
          clientFirstMount = function clientFirstMount() {
            normalizeComponents(router.currentRoute, router.currentRoute);
            showNextPage.call(_app, router.currentRoute); // Don't call fixPrepatch.call(_app, router.currentRoute, router.currentRoute) since it's first render

            mount();
          };

          render.call(_app, router.currentRoute, router.currentRoute, function (path) {
            // If not redirected
            if (!path) {
              clientFirstMount();
              return;
            } // Add a one-time afterEach hook to
            // mount the app wait for redirect and route gets resolved


            var unregisterHook = router.afterEach(function (to, from) {
              unregisterHook();
              clientFirstMount();
            }); // Push the path and let route to be resolved

            router.push(path, undefined, function (err) {
              if (err) {
                errorHandler(err);
              }
            });
          });

        case 18:
        case "end":
          return _context4.stop();
      }
    }
  });
}
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(41)))

/***/ }),

/***/ 155:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_error_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(71);
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_error_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_error_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */
 /* unused harmony default export */ var _unused_webpack_default_export = (_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_error_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 156:
/***/ (function(module, exports, __webpack_require__) {

// Imports
var ___CSS_LOADER_API_IMPORT___ = __webpack_require__(51);
exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
exports.push([module.i, ".__nuxt-error-page{padding:1rem;background:#f7f8fb;color:#47494e;text-align:center;display:-webkit-box;display:flex;-webkit-box-pack:center;justify-content:center;-webkit-box-align:center;align-items:center;-webkit-box-orient:vertical;-webkit-box-direction:normal;flex-direction:column;font-family:sans-serif;font-weight:100!important;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;-webkit-font-smoothing:antialiased;position:absolute;top:0;left:0;right:0;bottom:0}.__nuxt-error-page .error{max-width:450px}.__nuxt-error-page .title{font-size:1.5rem;margin-top:15px;color:#47494e;margin-bottom:8px}.__nuxt-error-page .description{color:#7f828b;line-height:21px;margin-bottom:10px}.__nuxt-error-page a{color:#7f828b!important;text-decoration:none}.__nuxt-error-page .logo{position:fixed;left:12px;bottom:12px}", ""]);
// Exports
module.exports = exports;


/***/ }),

/***/ 157:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_loading_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(72);
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_loading_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_loading_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */
 /* unused harmony default export */ var _unused_webpack_default_export = (_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_nuxt_loading_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 158:
/***/ (function(module, exports, __webpack_require__) {

// Imports
var ___CSS_LOADER_API_IMPORT___ = __webpack_require__(51);
exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
exports.push([module.i, ".nuxt-progress{position:fixed;top:0;left:0;right:0;height:2px;width:0;opacity:1;-webkit-transition:width .1s,opacity .4s;transition:width .1s,opacity .4s;background-color:#fff;z-index:999999}.nuxt-progress.nuxt-progress-notransition{-webkit-transition:none;transition:none}.nuxt-progress-failed{background-color:red}", ""]);
// Exports
module.exports = exports;


/***/ }),

/***/ 159:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_default_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(73);
/* harmony import */ var _node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_default_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_default_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */
 /* unused harmony default export */ var _unused_webpack_default_export = (_node_modules_vue_style_loader_index_js_ref_3_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_3_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_3_oneOf_1_2_node_modules_vuetify_loader_lib_loader_js_ref_16_0_node_modules_vue_loader_lib_index_js_vue_loader_options_default_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 160:
/***/ (function(module, exports, __webpack_require__) {

// Imports
var ___CSS_LOADER_API_IMPORT___ = __webpack_require__(51);
exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
exports.push([module.i, "html{font-family:Source Sans Pro,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;font-size:16px;word-spacing:1px;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%;-moz-osx-font-smoothing:grayscale;-webkit-font-smoothing:antialiased;box-sizing:border-box}*,:after,:before{box-sizing:border-box;margin:0}.button--purple{display:inline-block;border-radius:4px;border:1px solid #0c1439;color:#0c1439;text-decoration:none;padding:10px 30px}.button--purple:hover{color:#fff;background-color:#0c1439}.button--grey{display:inline-block;border-radius:4px;border:1px solid #35495e;color:#35495e;text-decoration:none;padding:10px 30px;margin-left:15px}.button--grey:hover{color:#fff;background-color:#35495e}", ""]);
// Exports
module.exports = exports;


/***/ }),

/***/ 25:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";

// EXTERNAL MODULE: ./node_modules/core-js/modules/es7.object.get-own-property-descriptors.js
var es7_object_get_own_property_descriptors = __webpack_require__(33);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.symbol.js
var es6_symbol = __webpack_require__(21);

// EXTERNAL MODULE: ./node_modules/core-js/modules/web.dom.iterable.js
var web_dom_iterable = __webpack_require__(13);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.object.keys.js
var es6_object_keys = __webpack_require__(22);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.object.to-string.js
var es6_object_to_string = __webpack_require__(7);

// EXTERNAL MODULE: ./node_modules/regenerator-runtime/runtime.js
var runtime = __webpack_require__(59);

// EXTERNAL MODULE: ./node_modules/@babel/runtime/helpers/esm/defineProperty.js
var defineProperty = __webpack_require__(8);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.function.name.js
var es6_function_name = __webpack_require__(26);

// EXTERNAL MODULE: ./node_modules/vue/dist/vue.runtime.esm.js
var vue_runtime_esm = __webpack_require__(0);

// EXTERNAL MODULE: ./node_modules/vue-meta/dist/vue-meta.esm.browser.js
var vue_meta_esm_browser = __webpack_require__(116);

// EXTERNAL MODULE: ./node_modules/vue-client-only/dist/vue-client-only.common.js
var vue_client_only_common = __webpack_require__(88);
var vue_client_only_common_default = /*#__PURE__*/__webpack_require__.n(vue_client_only_common);

// EXTERNAL MODULE: ./node_modules/vue-no-ssr/dist/vue-no-ssr.common.js
var vue_no_ssr_common = __webpack_require__(40);
var vue_no_ssr_common_default = /*#__PURE__*/__webpack_require__.n(vue_no_ssr_common);

// EXTERNAL MODULE: ./node_modules/vue-router/dist/vue-router.esm.js
var vue_router_esm = __webpack_require__(50);

// EXTERNAL MODULE: ./.nuxt/utils.js
var utils = __webpack_require__(1);

// CONCATENATED MODULE: ./.nuxt/router.scrollBehavior.js



if (true) {
  if ('scrollRestoration' in window.history) {
    window.history.scrollRestoration = 'manual'; // reset scrollRestoration to auto when leaving page, allowing page reload
    // and back-navigation from other pages to use the browser to restore the
    // scrolling position.

    window.addEventListener('beforeunload', function () {
      window.history.scrollRestoration = 'auto';
    }); // Setting scrollRestoration to manual again when returning to this page.

    window.addEventListener('load', function () {
      window.history.scrollRestoration = 'manual';
    });
  }
}

/* harmony default export */ var router_scrollBehavior = (function (to, from, savedPosition) {
  // if the returned position is falsy or an empty object,
  // will retain current scroll position.
  var position = false; // if no children detected and scrollToTop is not explicitly disabled

  var Pages = Object(utils["e" /* getMatchedComponents */])(to);

  if (Pages.length < 2 && Pages.every(function (Page) {
    return Page.options.scrollToTop !== false;
  })) {
    // scroll to the top of the page
    position = {
      x: 0,
      y: 0
    };
  } else if (Pages.some(function (Page) {
    return Page.options.scrollToTop;
  })) {
    // if one of the children has scrollToTop option set to true
    position = {
      x: 0,
      y: 0
    };
  } // savedPosition is only available for popstate navigations (back button)


  if (savedPosition) {
    position = savedPosition;
  }

  var nuxt = window.$nuxt; // triggerScroll is only fired when a new component is loaded

  if (to.path === from.path && to.hash !== from.hash) {
    nuxt.$nextTick(function () {
      return nuxt.$emit('triggerScroll');
    });
  }

  return new Promise(function (resolve) {
    // wait for the out transition to complete (if necessary)
    nuxt.$once('triggerScroll', function () {
      // coords will be used if no selector is provided,
      // or if the selector didn't match any element.
      if (to.hash) {
        var hash = to.hash; // CSS.escape() is not supported with IE and Edge.

        if (typeof window.CSS !== 'undefined' && typeof window.CSS.escape !== 'undefined') {
          hash = '#' + window.CSS.escape(hash.substr(1));
        }

        try {
          if (document.querySelector(hash)) {
            // scroll to anchor by returning the selector
            position = {
              selector: hash
            };
          }
        } catch (e) {
          console.warn('Failed to save scroll position. Please add CSS.escape() polyfill (https://github.com/mathiasbynens/CSS.escape).');
        }
      }

      resolve(position);
    });
  });
});
// CONCATENATED MODULE: ./.nuxt/router.js





var router_a6dd86aa = function _a6dd86aa() {
  return Object(utils["j" /* interopDefault */])(Promise.all(/* import() | pages/docs/index */[__webpack_require__.e(0), __webpack_require__.e(5)]).then(__webpack_require__.bind(null, 383)));
};

var router_aa4ac93a = function _aa4ac93a() {
  return Object(utils["j" /* interopDefault */])(Promise.all(/* import() | pages/docs/_slug */[__webpack_require__.e(0), __webpack_require__.e(9), __webpack_require__.e(4)]).then(__webpack_require__.bind(null, 386)));
};

var router_a574ef96 = function _a574ef96() {
  return Object(utils["j" /* interopDefault */])(__webpack_require__.e(/* import() | pages/index */ 6).then(__webpack_require__.bind(null, 384)));
};

var router_363b227c = function _363b227c() {
  return Object(utils["j" /* interopDefault */])(__webpack_require__.e(/* import() | pages/_ */ 3).then(__webpack_require__.bind(null, 385)));
}; // TODO: remove in Nuxt 3


var emptyFn = function emptyFn() {};

var originalPush = vue_router_esm["a" /* default */].prototype.push;

vue_router_esm["a" /* default */].prototype.push = function push(location) {
  var onComplete = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : emptyFn;
  var onAbort = arguments.length > 2 ? arguments[2] : undefined;
  return originalPush.call(this, location, onComplete, onAbort);
};

vue_runtime_esm["a" /* default */].use(vue_router_esm["a" /* default */]);
var routerOptions = {
  mode: 'history',
  base: decodeURI('/'),
  linkActiveClass: 'nuxt-link-active',
  linkExactActiveClass: 'nuxt-link-exact-active',
  scrollBehavior: router_scrollBehavior,
  routes: [{
    path: "/docs",
    component: router_a6dd86aa,
    name: "docs"
  }, {
    path: "/docs/:slug",
    component: router_aa4ac93a,
    name: "docs-slug"
  }, {
    path: "/",
    component: router_a574ef96,
    name: "index"
  }, {
    path: "/*",
    component: router_363b227c,
    name: "all"
  }],
  fallback: false
};
function createRouter() {
  return new vue_router_esm["a" /* default */](routerOptions);
}
// CONCATENATED MODULE: ./.nuxt/components/nuxt-child.js
/* harmony default export */ var nuxt_child = ({
  name: 'NuxtChild',
  functional: true,
  props: {
    nuxtChildKey: {
      type: String,
      default: ''
    },
    keepAlive: Boolean,
    keepAliveProps: {
      type: Object,
      default: undefined
    }
  },
  render: function render(h, _ref) {
    var parent = _ref.parent,
        data = _ref.data,
        props = _ref.props;
    data.nuxtChild = true;
    var _parent = parent;
    var transitions = parent.$nuxt.nuxt.transitions;
    var defaultTransition = parent.$nuxt.nuxt.defaultTransition;
    var depth = 0;

    while (parent) {
      if (parent.$vnode && parent.$vnode.data.nuxtChild) {
        depth++;
      }

      parent = parent.$parent;
    }

    data.nuxtChildDepth = depth;
    var transition = transitions[depth] || defaultTransition;
    var transitionProps = {};
    transitionsKeys.forEach(function (key) {
      if (typeof transition[key] !== 'undefined') {
        transitionProps[key] = transition[key];
      }
    });
    var listeners = {};
    listenersKeys.forEach(function (key) {
      if (typeof transition[key] === 'function') {
        listeners[key] = transition[key].bind(_parent);
      }
    }); // Add triggerScroll event on beforeEnter (fix #1376)

    var beforeEnter = listeners.beforeEnter;

    listeners.beforeEnter = function (el) {
      // Ensure to trigger scroll event after calling scrollBehavior
      window.$nuxt.$nextTick(function () {
        window.$nuxt.$emit('triggerScroll');
      });

      if (beforeEnter) {
        return beforeEnter.call(_parent, el);
      }
    }; // make sure that leave is called asynchronous (fix #5703)


    if (transition.css === false) {
      var leave = listeners.leave; // only add leave listener when user didnt provide one
      // or when it misses the done argument

      if (!leave || leave.length < 2) {
        listeners.leave = function (el, done) {
          if (leave) {
            leave.call(_parent, el);
          }

          _parent.$nextTick(done);
        };
      }
    }

    var routerView = h('routerView', data);

    if (props.keepAlive) {
      routerView = h('keep-alive', {
        props: props.keepAliveProps
      }, [routerView]);
    }

    return h('transition', {
      props: transitionProps,
      on: listeners
    }, [routerView]);
  }
});
var transitionsKeys = ['name', 'mode', 'appear', 'css', 'type', 'duration', 'enterClass', 'leaveClass', 'appearClass', 'enterActiveClass', 'enterActiveClass', 'leaveActiveClass', 'appearActiveClass', 'enterToClass', 'leaveToClass', 'appearToClass'];
var listenersKeys = ['beforeEnter', 'enter', 'afterEnter', 'enterCancelled', 'beforeLeave', 'leave', 'afterLeave', 'leaveCancelled', 'beforeAppear', 'appear', 'afterAppear', 'appearCancelled'];
// CONCATENATED MODULE: ./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./.nuxt/components/nuxt-error.vue?vue&type=template&id=e0c331e2&
var nuxt_errorvue_type_template_id_e0c331e2_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"__nuxt-error-page"},[_c('div',{staticClass:"error"},[_c('svg',{attrs:{"xmlns":"http://www.w3.org/2000/svg","width":"90","height":"90","fill":"#DBE1EC","viewBox":"0 0 48 48"}},[_c('path',{attrs:{"d":"M22 30h4v4h-4zm0-16h4v12h-4zm1.99-10C12.94 4 4 12.95 4 24s8.94 20 19.99 20S44 35.05 44 24 35.04 4 23.99 4zM24 40c-8.84 0-16-7.16-16-16S15.16 8 24 8s16 7.16 16 16-7.16 16-16 16z"}})]),_vm._v(" "),_c('div',{staticClass:"title"},[_vm._v(_vm._s(_vm.message))]),_vm._v(" "),(_vm.statusCode === 404)?_c('p',{staticClass:"description"},[_c('NuxtLink',{staticClass:"error-link",attrs:{"to":"/"}},[_vm._v("Back to the home page")])],1):_vm._e(),_vm._v(" "),_vm._m(0)])])}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"logo"},[_c('a',{attrs:{"href":"https://nuxtjs.org","target":"_blank","rel":"noopener"}},[_vm._v("Nuxt.js")])])}]


// CONCATENATED MODULE: ./.nuxt/components/nuxt-error.vue?vue&type=template&id=e0c331e2&

// CONCATENATED MODULE: ./node_modules/babel-loader/lib??ref--2-0!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./.nuxt/components/nuxt-error.vue?vue&type=script&lang=js&
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
/* harmony default export */ var nuxt_errorvue_type_script_lang_js_ = ({
  name: 'NuxtError',
  props: {
    error: {
      type: Object,
      default: null
    }
  },
  computed: {
    statusCode: function statusCode() {
      return this.error && this.error.statusCode || 500;
    },
    message: function message() {
      return this.error.message || 'Error';
    }
  },
  head: function head() {
    return {
      title: this.message,
      meta: [{
        name: 'viewport',
        content: 'width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no'
      }]
    };
  }
});
// CONCATENATED MODULE: ./.nuxt/components/nuxt-error.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_nuxt_errorvue_type_script_lang_js_ = (nuxt_errorvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./.nuxt/components/nuxt-error.vue?vue&type=style&index=0&lang=css&
var nuxt_errorvue_type_style_index_0_lang_css_ = __webpack_require__(155);

// EXTERNAL MODULE: ./node_modules/vue-loader/lib/runtime/componentNormalizer.js
var componentNormalizer = __webpack_require__(38);

// CONCATENATED MODULE: ./.nuxt/components/nuxt-error.vue






/* normalize component */

var component = Object(componentNormalizer["a" /* default */])(
  components_nuxt_errorvue_type_script_lang_js_,
  nuxt_errorvue_type_template_id_e0c331e2_render,
  staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var nuxt_error = (component.exports);
// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.regexp.to-string.js
var es6_regexp_to_string = __webpack_require__(56);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.date.to-string.js
var es6_date_to_string = __webpack_require__(57);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.regexp.replace.js
var es6_regexp_replace = __webpack_require__(39);

// EXTERNAL MODULE: ./node_modules/@babel/runtime/helpers/esm/slicedToArray.js + 3 modules
var slicedToArray = __webpack_require__(15);

// CONCATENATED MODULE: ./.nuxt/components/nuxt.js









/* harmony default export */ var components_nuxt = ({
  name: 'Nuxt',
  components: {
    NuxtChild: nuxt_child,
    NuxtError: nuxt_error
  },
  props: {
    nuxtChildKey: {
      type: String,
      default: undefined
    },
    keepAlive: Boolean,
    keepAliveProps: {
      type: Object,
      default: undefined
    },
    name: {
      type: String,
      default: 'default'
    }
  },
  errorCaptured: function errorCaptured(error) {
    // if we receive and error while showing the NuxtError component
    // capture the error and force an immediate update so we re-render
    // without the NuxtError component
    if (this.displayingNuxtError) {
      this.errorFromNuxtError = error;
      this.$forceUpdate();
    }
  },
  computed: {
    routerViewKey: function routerViewKey() {
      // If nuxtChildKey prop is given or current route has children
      if (typeof this.nuxtChildKey !== 'undefined' || this.$route.matched.length > 1) {
        return this.nuxtChildKey || Object(utils["b" /* compile */])(this.$route.matched[0].path)(this.$route.params);
      }

      var _this$$route$matched = Object(slicedToArray["a" /* default */])(this.$route.matched, 1),
          matchedRoute = _this$$route$matched[0];

      if (!matchedRoute) {
        return this.$route.path;
      }

      var Component = matchedRoute.components.default;

      if (Component && Component.options) {
        var options = Component.options;

        if (options.key) {
          return typeof options.key === 'function' ? options.key(this.$route) : options.key;
        }
      }

      var strict = /\/$/.test(matchedRoute.path);
      return strict ? this.$route.path : this.$route.path.replace(/\/$/, '');
    }
  },
  beforeCreate: function beforeCreate() {
    vue_runtime_esm["a" /* default */].util.defineReactive(this, 'nuxt', this.$root.$options.nuxt);
  },
  render: function render(h) {
    var _this = this;

    // if there is no error
    if (!this.nuxt.err) {
      // Directly return nuxt child
      return h('NuxtChild', {
        key: this.routerViewKey,
        props: this.$props
      });
    } // if an error occured within NuxtError show a simple
    // error message instead to prevent looping


    if (this.errorFromNuxtError) {
      this.$nextTick(function () {
        return _this.errorFromNuxtError = false;
      });
      return h('div', {}, [h('h2', 'An error occured while showing the error page'), h('p', 'Unfortunately an error occured and while showing the error page another error occured'), h('p', "Error details: ".concat(this.errorFromNuxtError.toString())), h('nuxt-link', {
        props: {
          to: '/'
        }
      }, 'Go back to home')]);
    } // track if we are showing the NuxtError component


    this.displayingNuxtError = true;
    this.$nextTick(function () {
      return _this.displayingNuxtError = false;
    });
    return h(nuxt_error, {
      props: {
        error: this.nuxt.err
      }
    });
  }
});
// EXTERNAL MODULE: ./node_modules/core-js/modules/es6.string.iterator.js
var es6_string_iterator = __webpack_require__(58);

// CONCATENATED MODULE: ./node_modules/babel-loader/lib??ref--2-0!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./.nuxt/components/nuxt-loading.vue?vue&type=script&lang=js&
/* harmony default export */ var nuxt_loadingvue_type_script_lang_js_ = ({
  name: 'NuxtLoading',
  data: function data() {
    return {
      percent: 0,
      show: false,
      canSucceed: true,
      reversed: false,
      skipTimerCount: 0,
      rtl: false,
      throttle: 200,
      duration: 5000,
      continuous: false
    };
  },
  computed: {
    left: function left() {
      if (!this.continuous && !this.rtl) {
        return false;
      }

      return this.rtl ? this.reversed ? '0px' : 'auto' : !this.reversed ? '0px' : 'auto';
    }
  },
  beforeDestroy: function beforeDestroy() {
    this.clear();
  },
  methods: {
    clear: function clear() {
      clearInterval(this._timer);
      clearTimeout(this._throttle);
      this._timer = null;
    },
    start: function start() {
      var _this = this;

      this.clear();
      this.percent = 0;
      this.reversed = false;
      this.skipTimerCount = 0;
      this.canSucceed = true;

      if (this.throttle) {
        this._throttle = setTimeout(function () {
          return _this.startTimer();
        }, this.throttle);
      } else {
        this.startTimer();
      }

      return this;
    },
    set: function set(num) {
      this.show = true;
      this.canSucceed = true;
      this.percent = Math.min(100, Math.max(0, Math.floor(num)));
      return this;
    },
    get: function get() {
      return this.percent;
    },
    increase: function increase(num) {
      this.percent = Math.min(100, Math.floor(this.percent + num));
      return this;
    },
    decrease: function decrease(num) {
      this.percent = Math.max(0, Math.floor(this.percent - num));
      return this;
    },
    pause: function pause() {
      clearInterval(this._timer);
      return this;
    },
    resume: function resume() {
      this.startTimer();
      return this;
    },
    finish: function finish() {
      this.percent = this.reversed ? 0 : 100;
      this.hide();
      return this;
    },
    hide: function hide() {
      var _this2 = this;

      this.clear();
      setTimeout(function () {
        _this2.show = false;

        _this2.$nextTick(function () {
          _this2.percent = 0;
          _this2.reversed = false;
        });
      }, 500);
      return this;
    },
    fail: function fail() {
      this.canSucceed = false;
      return this;
    },
    startTimer: function startTimer() {
      var _this3 = this;

      if (!this.show) {
        this.show = true;
      }

      if (typeof this._cut === 'undefined') {
        this._cut = 10000 / Math.floor(this.duration);
      }

      this._timer = setInterval(function () {
        /**
         * When reversing direction skip one timers
         * so 0, 100 are displayed for two iterations
         * also disable css width transitioning
         * which otherwise interferes and shows
         * a jojo effect
         */
        if (_this3.skipTimerCount > 0) {
          _this3.skipTimerCount--;
          return;
        }

        if (_this3.reversed) {
          _this3.decrease(_this3._cut);
        } else {
          _this3.increase(_this3._cut);
        }

        if (_this3.continuous) {
          if (_this3.percent >= 100) {
            _this3.skipTimerCount = 1;
            _this3.reversed = !_this3.reversed;
          } else if (_this3.percent <= 0) {
            _this3.skipTimerCount = 1;
            _this3.reversed = !_this3.reversed;
          }
        }
      }, 100);
    }
  },
  render: function render(h) {
    var el = h(false);

    if (this.show) {
      el = h('div', {
        staticClass: 'nuxt-progress',
        class: {
          'nuxt-progress-notransition': this.skipTimerCount > 0,
          'nuxt-progress-failed': !this.canSucceed
        },
        style: {
          width: this.percent + '%',
          left: this.left
        }
      });
    }

    return el;
  }
});
// CONCATENATED MODULE: ./.nuxt/components/nuxt-loading.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_nuxt_loadingvue_type_script_lang_js_ = (nuxt_loadingvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./.nuxt/components/nuxt-loading.vue?vue&type=style&index=0&lang=css&
var nuxt_loadingvue_type_style_index_0_lang_css_ = __webpack_require__(157);

// CONCATENATED MODULE: ./.nuxt/components/nuxt-loading.vue
var nuxt_loading_render, nuxt_loading_staticRenderFns





/* normalize component */

var nuxt_loading_component = Object(componentNormalizer["a" /* default */])(
  components_nuxt_loadingvue_type_script_lang_js_,
  nuxt_loading_render,
  nuxt_loading_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var nuxt_loading = (nuxt_loading_component.exports);
// CONCATENATED MODULE: ./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/vuetify-loader/lib/loader.js??ref--16-0!./node_modules/vue-loader/lib??vue-loader-options!./layouts/default.vue?vue&type=template&id=745d513a&
var defaultvue_type_template_id_745d513a_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('nuxt')],1)}
var defaultvue_type_template_id_745d513a_staticRenderFns = []


// CONCATENATED MODULE: ./layouts/default.vue?vue&type=template&id=745d513a&

// EXTERNAL MODULE: ./layouts/default.vue?vue&type=style&index=0&lang=css&
var defaultvue_type_style_index_0_lang_css_ = __webpack_require__(159);

// CONCATENATED MODULE: ./layouts/default.vue

var script = {}



/* normalize component */

var default_component = Object(componentNormalizer["a" /* default */])(
  script,
  defaultvue_type_template_id_745d513a_render,
  defaultvue_type_template_id_745d513a_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var layouts_default = (default_component.exports);
// CONCATENATED MODULE: ./.nuxt/App.js








var layouts = {
  "_default": layouts_default
};
/* harmony default export */ var App = ({
  head: {
    "title": "eb-docs",
    "meta": [{
      "charset": "utf-8"
    }, {
      "name": "viewport",
      "content": "width=device-width, initial-scale=1"
    }, {
      "hid": "description",
      "name": "description",
      "content": "docs for engineblock"
    }],
    "link": [{
      "rel": "icon",
      "type": "image/x-icon",
      "href": "/favicon.ico"
    }, {
      "rel": "stylesheet",
      "type": "text/css",
      "href": "https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900&display=swap"
    }, {
      "rel": "stylesheet",
      "type": "text/css",
      "href": "https://cdn.jsdelivr.net/npm/@mdi/font@latest/css/materialdesignicons.min.css"
    }],
    "style": [],
    "script": []
  },
  render: function render(h, props) {
    var loadingEl = h('NuxtLoading', {
      ref: 'loading'
    });
    var layoutEl = h(this.layout || 'nuxt');
    var templateEl = h('div', {
      domProps: {
        id: '__layout'
      },
      key: this.layoutName
    }, [layoutEl]);
    var transitionEl = h('transition', {
      props: {
        name: 'layout',
        mode: 'out-in'
      },
      on: {
        beforeEnter: function beforeEnter(el) {
          // Ensure to trigger scroll event after calling scrollBehavior
          window.$nuxt.$nextTick(function () {
            window.$nuxt.$emit('triggerScroll');
          });
        }
      }
    }, [templateEl]);
    return h('div', {
      domProps: {
        id: '__nuxt'
      }
    }, [loadingEl, transitionEl]);
  },
  data: function data() {
    return {
      isOnline: true,
      layout: null,
      layoutName: ''
    };
  },
  beforeCreate: function beforeCreate() {
    vue_runtime_esm["a" /* default */].util.defineReactive(this, 'nuxt', this.$options.nuxt);
  },
  created: function created() {
    // Add this.$nuxt in child instances
    vue_runtime_esm["a" /* default */].prototype.$nuxt = this; // add to window so we can listen when ready

    if (true) {
      window.$nuxt = this;
      this.refreshOnlineStatus(); // Setup the listeners

      window.addEventListener('online', this.refreshOnlineStatus);
      window.addEventListener('offline', this.refreshOnlineStatus);
    } // Add $nuxt.error()


    this.error = this.nuxt.error; // Add $nuxt.context

    this.context = this.$options.context;
  },
  mounted: function mounted() {
    this.$loading = this.$refs.loading;
  },
  watch: {
    'nuxt.err': 'errorChanged'
  },
  computed: {
    isOffline: function isOffline() {
      return !this.isOnline;
    }
  },
  methods: {
    refreshOnlineStatus: function refreshOnlineStatus() {
      if (true) {
        if (typeof window.navigator.onLine === 'undefined') {
          // If the browser doesn't support connection status reports
          // assume that we are online because most apps' only react
          // when they now that the connection has been interrupted
          this.isOnline = true;
        } else {
          this.isOnline = window.navigator.onLine;
        }
      }
    },
    refresh: function refresh() {
      var _this = this;

      var pages, promises;
      return regeneratorRuntime.async(function refresh$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              pages = Object(utils["f" /* getMatchedComponentsInstances */])(this.$route);

              if (pages.length) {
                _context.next = 3;
                break;
              }

              return _context.abrupt("return");

            case 3:
              this.$loading.start();
              promises = pages.map(function (page) {
                var p = [];

                if (page.$options.fetch) {
                  p.push(Object(utils["m" /* promisify */])(page.$options.fetch, _this.context));
                }

                if (page.$options.asyncData) {
                  p.push(Object(utils["m" /* promisify */])(page.$options.asyncData, _this.context).then(function (newData) {
                    for (var key in newData) {
                      vue_runtime_esm["a" /* default */].set(page.$data, key, newData[key]);
                    }
                  }));
                }

                return Promise.all(p);
              });
              _context.prev = 5;
              _context.next = 8;
              return regeneratorRuntime.awrap(Promise.all(promises));

            case 8:
              _context.next = 15;
              break;

            case 10:
              _context.prev = 10;
              _context.t0 = _context["catch"](5);
              this.$loading.fail();
              Object(utils["i" /* globalHandleError */])(_context.t0);
              this.error(_context.t0);

            case 15:
              this.$loading.finish();

            case 16:
            case "end":
              return _context.stop();
          }
        }
      }, null, this, [[5, 10]]);
    },
    errorChanged: function errorChanged() {
      if (this.nuxt.err && this.$loading) {
        if (this.$loading.fail) {
          this.$loading.fail();
        }

        if (this.$loading.finish) {
          this.$loading.finish();
        }
      }
    },
    setLayout: function setLayout(layout) {
      if (!layout || !layouts['_' + layout]) {
        layout = 'default';
      }

      this.layoutName = layout;
      this.layout = layouts['_' + layout];
      return this.layout;
    },
    loadLayout: function loadLayout(layout) {
      if (!layout || !layouts['_' + layout]) {
        layout = 'default';
      }

      return Promise.resolve(layouts['_' + layout]);
    }
  },
  components: {
    NuxtLoading: nuxt_loading
  }
});
// EXTERNAL MODULE: ./node_modules/vuetify/lib/framework.js + 21 modules
var framework = __webpack_require__(175);

// CONCATENATED MODULE: ./.nuxt/vuetify/options.js
/* harmony default export */ var vuetify_options = ({
  "theme": {
    "dark": true,
    "themes": {
      "dark": {
        "primary": "#FF7D2B",
        "secondary": "#0C153A",
        "accent": "#FF7D2B"
      }
    }
  }
});
// CONCATENATED MODULE: ./.nuxt/vuetify/plugin.js



vue_runtime_esm["a" /* default */].use(framework["a" /* default */], {});
/* harmony default export */ var vuetify_plugin = (function (ctx) {
  var vuetifyOptions = typeof vuetify_options === 'function' ? vuetify_options(ctx) : vuetify_options;
  vuetifyOptions.icons = vuetifyOptions.icons || {};
  vuetifyOptions.icons.iconfont = 'mdi';
  var vuetify = new framework["a" /* default */](vuetifyOptions);
  ctx.app.vuetify = vuetify;
  ctx.$vuetify = vuetify.framework;
});
// CONCATENATED MODULE: ./.nuxt/index.js
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return createApp; });
/* concated harmony reexport NuxtError */__webpack_require__.d(__webpack_exports__, "a", function() { return nuxt_error; });









function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { Object(defineProperty["a" /* default */])(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }











/* Plugins */

 // Source: ./vuetify/plugin.js (mode: 'all')
// Component: <ClientOnly>

vue_runtime_esm["a" /* default */].component(vue_client_only_common_default.a.name, vue_client_only_common_default.a); // TODO: Remove in Nuxt 3: <NoSsr>

vue_runtime_esm["a" /* default */].component(vue_no_ssr_common_default.a.name, _objectSpread({}, vue_no_ssr_common_default.a, {
  render: function render(h, ctx) {
    if ( true && !vue_no_ssr_common_default.a._warned) {
      vue_no_ssr_common_default.a._warned = true;
      console.warn('<no-ssr> has been deprecated and will be removed in Nuxt 3, please use <client-only> instead');
    }

    return vue_no_ssr_common_default.a.render(h, ctx);
  }
})); // Component: <NuxtChild>

vue_runtime_esm["a" /* default */].component(nuxt_child.name, nuxt_child);
vue_runtime_esm["a" /* default */].component('NChild', nuxt_child); // Component NuxtLink is imported in server.js or client.js
// Component: <Nuxt>

vue_runtime_esm["a" /* default */].component(components_nuxt.name, components_nuxt);
vue_runtime_esm["a" /* default */].use(vue_meta_esm_browser["a" /* default */], {
  "keyName": "head",
  "attribute": "data-n-head",
  "ssrAttribute": "data-n-head-ssr",
  "tagIDKeyName": "hid"
});
var defaultTransition = {
  "name": "page",
  "mode": "out-in",
  "appear": true,
  "appearClass": "appear",
  "appearActiveClass": "appear-active",
  "appearToClass": "appear-to"
};

function createApp(ssrContext) {
  var router, app, next, route, path, inject;
  return regeneratorRuntime.async(function createApp$(_context2) {
    while (1) {
      switch (_context2.prev = _context2.next) {
        case 0:
          _context2.next = 2;
          return regeneratorRuntime.awrap(createRouter(ssrContext));

        case 2:
          router = _context2.sent;
          // Create Root instance
          // here we inject the router and store to all child components,
          // making them available everywhere as `this.$router` and `this.$store`.
          app = _objectSpread({
            router: router,
            nuxt: {
              defaultTransition: defaultTransition,
              transitions: [defaultTransition],
              setTransitions: function setTransitions(transitions) {
                if (!Array.isArray(transitions)) {
                  transitions = [transitions];
                }

                transitions = transitions.map(function (transition) {
                  if (!transition) {
                    transition = defaultTransition;
                  } else if (typeof transition === 'string') {
                    transition = Object.assign({}, defaultTransition, {
                      name: transition
                    });
                  } else {
                    transition = Object.assign({}, defaultTransition, transition);
                  }

                  return transition;
                });
                this.$options.nuxt.transitions = transitions;
                return transitions;
              },
              err: null,
              dateErr: null,
              error: function error(err) {
                err = err || null;
                app.context._errored = Boolean(err);
                err = err ? Object(utils["l" /* normalizeError */])(err) : null;
                var nuxt = this.nuxt || this.$options.nuxt;
                nuxt.dateErr = Date.now();
                nuxt.err = err; // Used in src/server.js

                if (ssrContext) {
                  ssrContext.nuxt.error = err;
                }

                return err;
              }
            }
          }, App);
          next = ssrContext ? ssrContext.next : function (location) {
            return app.router.push(location);
          }; // Resolve route

          if (ssrContext) {
            route = router.resolve(ssrContext.url).route;
          } else {
            path = Object(utils["d" /* getLocation */])(router.options.base, router.options.mode);
            route = router.resolve(path).route;
          } // Set context to app.context


          _context2.next = 8;
          return regeneratorRuntime.awrap(Object(utils["p" /* setContext */])(app, {
            route: route,
            next: next,
            error: app.nuxt.error.bind(app),
            payload: ssrContext ? ssrContext.payload : undefined,
            req: ssrContext ? ssrContext.req : undefined,
            res: ssrContext ? ssrContext.res : undefined,
            beforeRenderFns: ssrContext ? ssrContext.beforeRenderFns : undefined,
            ssrContext: ssrContext
          }));

        case 8:
          inject = function inject(key, value) {
            if (!key) {
              throw new Error('inject(key, value) has no key provided');
            }

            if (value === undefined) {
              throw new Error('inject(key, value) has no value provided');
            }

            key = '$' + key; // Add into app

            app[key] = value; // Check if plugin not already installed

            var installKey = '__nuxt_' + key + '_installed__';

            if (vue_runtime_esm["a" /* default */][installKey]) {
              return;
            }

            vue_runtime_esm["a" /* default */][installKey] = true; // Call Vue.use() to install the plugin into vm

            vue_runtime_esm["a" /* default */].use(function () {
              if (!Object.prototype.hasOwnProperty.call(vue_runtime_esm["a" /* default */], key)) {
                Object.defineProperty(vue_runtime_esm["a" /* default */].prototype, key, {
                  get: function get() {
                    return this.$root.$options[key];
                  }
                });
              }
            });
          }; // Plugin execution


          if (!(typeof vuetify_plugin === 'function')) {
            _context2.next = 12;
            break;
          }

          _context2.next = 12;
          return regeneratorRuntime.awrap(vuetify_plugin(app.context, inject));

        case 12:
          if (true) {
            _context2.next = 15;
            break;
          }

          _context2.next = 15;
          return regeneratorRuntime.awrap(new Promise(function (resolve, reject) {
            router.push(ssrContext.url, resolve, function () {
              // navigated to a different route in router guard
              var unregister = router.afterEach(function _callee(to, from, next) {
                return regeneratorRuntime.async(function _callee$(_context) {
                  while (1) {
                    switch (_context.prev = _context.next) {
                      case 0:
                        ssrContext.url = to.fullPath;
                        _context.next = 3;
                        return regeneratorRuntime.awrap(Object(utils["h" /* getRouteData */])(to));

                      case 3:
                        app.context.route = _context.sent;
                        app.context.params = to.params || {};
                        app.context.query = to.query || {};
                        unregister();
                        resolve();

                      case 8:
                      case "end":
                        return _context.stop();
                    }
                  }
                });
              });
            });
          }));

        case 15:
          return _context2.abrupt("return", {
            app: app,
            router: router
          });

        case 16:
        case "end":
          return _context2.stop();
      }
    }
  });
}



/***/ }),

/***/ 71:
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(156);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var add = __webpack_require__(52).default
var update = add("7891058d", content, true, {"sourceMap":false});

/***/ }),

/***/ 72:
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(158);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var add = __webpack_require__(52).default
var update = add("5b425f4d", content, true, {"sourceMap":false});

/***/ }),

/***/ 73:
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(160);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var add = __webpack_require__(52).default
var update = add("262847ae", content, true, {"sourceMap":false});

/***/ }),

/***/ 74:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(7);
/* harmony import */ var core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_object_to_string__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(75);
/* harmony import */ var core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(21);
/* harmony import */ var core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(13);
/* harmony import */ var core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(53);
/* harmony import */ var core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(54);
/* harmony import */ var core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es6_string_includes__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var vue__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(0);








var requestIdleCallback = window.requestIdleCallback || function (cb) {
  var start = Date.now();
  return setTimeout(function () {
    cb({
      didTimeout: false,
      timeRemaining: function timeRemaining() {
        return Math.max(0, 50 - (Date.now() - start));
      }
    });
  }, 1);
};

var cancelIdleCallback = window.cancelIdleCallback || function (id) {
  clearTimeout(id);
};

var observer = window.IntersectionObserver && new window.IntersectionObserver(function (entries) {
  entries.forEach(function (_ref) {
    var intersectionRatio = _ref.intersectionRatio,
        link = _ref.target;

    if (intersectionRatio <= 0) {
      return;
    }

    link.__prefetch();
  });
});
/* harmony default export */ __webpack_exports__["a"] = ({
  name: 'NuxtLink',
  extends: vue__WEBPACK_IMPORTED_MODULE_6__[/* default */ "a"].component('RouterLink'),
  props: {
    prefetch: {
      type: Boolean,
      default: true
    },
    noPrefetch: {
      type: Boolean,
      default: false
    }
  },
  mounted: function mounted() {
    if (this.prefetch && !this.noPrefetch) {
      this.handleId = requestIdleCallback(this.observe, {
        timeout: 2e3
      });
    }
  },
  beforeDestroy: function beforeDestroy() {
    cancelIdleCallback(this.handleId);

    if (this.__observed) {
      observer.unobserve(this.$el);
      delete this.$el.__prefetch;
    }
  },
  methods: {
    observe: function observe() {
      // If no IntersectionObserver, avoid prefetching
      if (!observer) {
        return;
      } // Add to observer


      if (this.shouldPrefetch()) {
        this.$el.__prefetch = this.prefetchLink.bind(this);
        observer.observe(this.$el);
        this.__observed = true;
      }
    },
    shouldPrefetch: function shouldPrefetch() {
      return this.getPrefetchComponents().length > 0;
    },
    canPrefetch: function canPrefetch() {
      var conn = navigator.connection;
      var hasBadConnection = this.$nuxt.isOffline || conn && ((conn.effectiveType || '').includes('2g') || conn.saveData);
      return !hasBadConnection;
    },
    getPrefetchComponents: function getPrefetchComponents() {
      var ref = this.$router.resolve(this.to, this.$route, this.append);
      var Components = ref.resolved.matched.map(function (r) {
        return r.components.default;
      });
      return Components.filter(function (Component) {
        return typeof Component === 'function' && !Component.options && !Component.__prefetched;
      });
    },
    prefetchLink: function prefetchLink() {
      if (!this.canPrefetch()) {
        return;
      } // Stop observing this link (in case of internet connection changes)


      observer.unobserve(this.$el);
      var Components = this.getPrefetchComponents();
      var _iteratorNormalCompletion = true;
      var _didIteratorError = false;
      var _iteratorError = undefined;

      try {
        for (var _iterator = Components[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
          var Component = _step.value;
          var componentOrPromise = Component();

          if (componentOrPromise instanceof Promise) {
            componentOrPromise.catch(function () {});
          }

          Component.__prefetched = true;
        }
      } catch (err) {
        _didIteratorError = true;
        _iteratorError = err;
      } finally {
        try {
          if (!_iteratorNormalCompletion && _iterator.return != null) {
            _iterator.return();
          }
        } finally {
          if (_didIteratorError) {
            throw _iteratorError;
          }
        }
      }
    }
  }
});

/***/ }),

/***/ 87:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var middleware = {};
/* harmony default export */ __webpack_exports__["a"] = (middleware);

/***/ })

},[[128,7,2,8]]]);