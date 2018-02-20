if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('sw.js').then(function(registration) {
  }).catch(function(err) {
    // registration failed :(
    console.error('ServiceWorker registration failed: ', err);
  });
}