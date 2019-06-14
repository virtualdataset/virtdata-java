
## Using Mustache Templating

This doc system comes pre-wired to work with both
markdown and mustache as rendering layers. That means
that you can use either markdown or mustache to edit
or template content.

[Mustache Docs](https://mustache.github.io/mustache.5.html)

However, the mustache templates render markdown by default.
If the need arises to template html directly from mustache,
then this will be added at that time.

## Examples

### &#123;&#123; files &#125;&#125; as markdown
```
{{ files }}
```

### &#123;&#123; path &#125;&#125; as markdown
```
{{ path }}
```

### &#123;&#123; files &#125;&#125; as html
{{ files }}


