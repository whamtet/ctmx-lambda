# CTMX Lambda

### Usage

Place the following at the bottom of your pages


```html
<script src="https://unpkg.com/htmx.org@1.2.0"></script>
<script>
  var root = 'https://lambda-url'; // your lambda-url here
  function onEvent(evtName, evt) {
    if (evtName === 'htmx:configRequest') {
      evt.detail.path = root + evt.detail.path;
    }
  }
  htmx.defineExtension('lambda-cors', {onEvent: onEvent});
</script>
```

Make sure you have [leiningen](https://leiningen.org/) installed then compile the clojurescript

    scripts/release

To print all the static html (prerendering)

    echo "console.log(require('./build/clojurescript/main/functions').static);" | node

