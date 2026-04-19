window.onload = function() {
    
      //<editor-fold desc="Changeable Configuration Block">
      window.ui = SwaggerUIBundle({
        urls: [
          {"url":"http://localhost:8080/events/v3/api-docs","name":"Events Service"},
          {"url":"http://localhost:8080/notifications/v3/api-docs","name":"Notifications Service"},
          {"url":"http://localhost:8080/users/v3/api-docs","name":"Users Service"},
          {"url":"http://localhost:8080/ticketing/v3/api-docs","name":"Ticketing Service"},
          {"url":"http://localhost:8080/payments/v3/api-docs","name":"Payments Service"}
        ],
        "dom_id": "#swagger-ui",
        deepLinking: true,
        presets: [
          SwaggerUIBundle.presets.apis,
          SwaggerUIStandalonePreset
        ],
        plugins: [
          SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout",
        queryConfigEnabled: false,
        requestInterceptor: function(request) {
          if (request.url.startsWith('http://localhost/events')) {
            request.url = request.url.replace('http://localhost', 'http://localhost:8080');
          } else if (request.url.startsWith('http://localhost/notifications')) {
            request.url = request.url.replace('http://localhost', 'http://localhost:8080');
          } else if (request.url.startsWith('http://localhost/users')) {
            request.url = request.url.replace('http://localhost', 'http://localhost:8080');
          } else if (request.url.startsWith('http://localhost/ticketing')) {
            request.url = request.url.replace('http://localhost', 'http://localhost:8080');
          } else if (request.url.startsWith('http://localhost/payments')) {
            request.url = request.url.replace('http://localhost', 'http://localhost:8080');
          }
          return request;
        }
      })
      
      //</editor-fold>

};