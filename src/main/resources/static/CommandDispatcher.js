/**
 * ViewConfig - Configuración de ciclo de vida de una vista
 */
class ViewConfig {
  constructor(viewName, dispatcher, config = {}) {
    this.viewName = viewName;
    this.dispatcher = dispatcher;
    this.urlInicio = config.urlInicio;
    this.urlCierre = config.urlCierre;
    this.dataInicio = config.dataInicio;
    this.onError = config.onError;
    this.onException = config.onException;
    this.handlers = new Map();
    this.contextIds = [];
    this.cleanup = null;
    this.started = false;
  }

  handler(commandName, handlerFn) {
    if (!this.handlers.has(commandName)) {
      this.handlers.set(commandName, []);
    }
    this.handlers.get(commandName).push(handlerFn);
    return handlerFn;
  }

  _start() {
    if (this.started && this.cleanup) {
      this.cleanup();
      this.contextIds = [];
    }
    const handlersObj = {};
    this.handlers.forEach((handlerArray, commandName) => {
      if (handlerArray.length === 1) {
        handlersObj[commandName] = handlerArray[0];
      } else {
        handlersObj[commandName] = (parametro) => {
          handlerArray.forEach(handler => {
            try { handler(parametro); } catch (e) { console.error(`Error en handler '${commandName}':`, e); }
          });
        };
      }
    });
    this.cleanup = this.dispatcher.mapFunctions(handlersObj, {
      onError: this.onError,
      onException: this.onException
    });
    if (this.urlInicio) {
      this.dispatcher.submit(this.urlInicio, this.dataInicio || {}, 'GET');
    }
    if (this.urlCierre) {
      this.dispatcher.registerCloseView(this.urlCierre, {}, this.cleanup);
    }
    this.started = true;
  }

  _finish() {
    if (this.urlCierre) {
      this.dispatcher.submit(this.urlCierre, {}, 'POST');
      this.urlCierre = null;
    }
    if (this.cleanup) {
      this.cleanup();
      this.cleanup = null;
    }
    this.contextIds = [];
    this.started = false;
    this.dispatcher.views.delete(this.viewName);
  }
}

class CommandDispatcher {
  constructor() {
    this.contexts = new Map();
    this.cleanupCallbacks = [];
    this.closeViewUrl = null;
    this.beforeUnloadRegistered = false;
    this.views = new Map();
  }

  mapFunctions(handlers, options = {}) {
    const contextId = Symbol('context');
    const functionsMap = new Map();
    Object.entries(handlers).forEach(([name, handler]) => {
      if (typeof handler === 'function') {
        functionsMap.set(name, handler);
      } else {
        console.warn(`Handler para '${name}' no es una función`);
      }
    });
    this.contexts.set(contextId, {
      functions: functionsMap,
      onError: options.onError,
      onException: options.onException
    });
    return () => { this.contexts.delete(contextId); };
  }

  mapFunction(commandName, handler) {
    return this.mapFunctions({ [commandName]: handler });
  }

  clearAll() { this.contexts.clear(); }

  registerView(viewName, config = {}) {
    const view = new ViewConfig(viewName, this, config);
    this.views.set(viewName, view);
    return view;
  }

  startView(viewConfig) { viewConfig._start(); }
  finishView(viewConfig) { viewConfig._finish(); }

  notifyClose(url, data = {}) {
    let bodyData;
    if (typeof data === 'string') {
      bodyData = data;
    } else {
      const urlEncodedData = new URLSearchParams();
      Object.entries(data).forEach(([key, value]) => {
        if (value !== undefined && value !== null) urlEncodedData.append(key, value);
      });
      bodyData = urlEncodedData.toString();
    }
    if (typeof navigator !== 'undefined' && navigator.sendBeacon) {
      navigator.sendBeacon(url, bodyData);
    } else {
      fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: bodyData, keepalive: true })
        .catch(e => console.error('Error en notifyClose:', e));
    }
  }

  registerCloseView(url, data = {}, onBeforeClose = null) {
    this.closeViewUrl = { url, data };
    if (onBeforeClose) this.cleanupCallbacks.push(onBeforeClose);
    this._ensureBeforeUnloadRegistered();
  }

  addCleanupCallback(callback) {
    if (typeof callback === 'function') {
      this.cleanupCallbacks.push(callback);
      this._ensureBeforeUnloadRegistered();
    }
  }

  _ensureBeforeUnloadRegistered() {
    if (!this.beforeUnloadRegistered && typeof window !== 'undefined') {
      window.addEventListener('beforeunload', () => { this._handleBeforeUnload(); });
      this.beforeUnloadRegistered = true;
    }
  }

  _handleBeforeUnload() {
    this.views.forEach(view => {
      if (view.urlCierre) { this.notifyClose(view.urlCierre, {}); view.urlCierre = null; }
      if (view.cleanup) { try { view.cleanup(); } catch (e) { console.error(`Error en cleanup de vista '${view.viewName}':`, e); } }
    });
    this.cleanupCallbacks.forEach(callback => { try { callback(); } catch (e) { console.error('Error en cleanup callback:', e); } });
    if (this.closeViewUrl) this.notifyClose(this.closeViewUrl.url, this.closeViewUrl.data);
  }

  connectSSE(url) {
    const eventSource = new EventSource(url);
    eventSource.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        if (data.commands && Array.isArray(data.commands)) this._processCommands(data.commands);
      } catch (e) { console.error('Error procesando mensaje SSE:', e); }
    };
    eventSource.onerror = (error) => { console.error('Error en conexión SSE:', error); };
    this.addCleanupCallback(() => { eventSource.close(); });
    return eventSource;
  }

  processCommands(data) {
    if (!data?.commands || !Array.isArray(data.commands)) {
      console.error('Respuesta no cumple contrato Commands:', data);
      return;
    }
    this._processCommands(data.commands);
  }

  async submit(url, data = {}, method = 'POST') {
    let bodyData;
    const httpMethod = (method || 'POST').toUpperCase();
    if (typeof data === 'string') {
      bodyData = data;
    } else {
      const urlEncodedData = new URLSearchParams();
      Object.entries(data).forEach(([key, value]) => {
        if (value !== undefined && value !== null) urlEncodedData.append(key, value);
      });
      bodyData = urlEncodedData.toString();
    }
    try {
      let fetchUrl = url;
      const fetchOptions = { method: httpMethod, credentials: 'include' };
      if (httpMethod === 'GET') {
        if (bodyData && bodyData.length > 0) fetchUrl += (fetchUrl.includes('?') ? '&' : '?') + bodyData;
      } else {
        fetchOptions.headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
        fetchOptions.body = bodyData;
      }
      const response = await fetch(fetchUrl, fetchOptions);
      const status = response.status;
      const text = await response.text();
      if (status === 299) { this._notifyException(text); return; }
      if (status < 200 || status > 299) { this._notifyError(status, text, fetchUrl, bodyData); return; }
      try {
        if (!text || text.trim() === '') return;
        const json = JSON.parse(text);
        if (json.commands && Array.isArray(json.commands)) {
          this._processCommands(json.commands);
        } else {
          console.error('Respuesta no cumple contrato Commands:', json);
        }
      } catch (e) { console.error('Error al parsear JSON:', text, e); }
    } catch (error) { this._notifyError(0, error.message, url, bodyData); }
  }

  _processCommands(commands) {
    commands.forEach(command => {
      const { id, parametro } = command;
      const matchingHandlers = [];
      this.contexts.forEach(context => {
        const handler = context.functions.get(id);
        if (handler) matchingHandlers.push(handler);
      });
      if (matchingHandlers.length > 1 && console.debug) {
        console.debug(`ℹ️ Comando '${id}' ejecutándose en ${matchingHandlers.length} contextos`);
      }
      matchingHandlers.forEach(handler => {
        try { handler(parametro); } catch (e) { console.error(`Error ejecutando función '${id}':`, e); }
      });
      if (matchingHandlers.length === 0) {
        console.warn(`⚠️ Comando '${id}' no tiene handlers registrados`, parametro);
      }
    });
  }

  _notifyError(status, text, url, data) {
    console.error('Error en submit:', { status, text, url, data });
    let notified = false;
    this.contexts.forEach(context => {
      if (context.onError && typeof context.onError === 'function') {
        try { context.onError(status, text); notified = true; } catch (e) { console.error('Error en handler de errores:', e); }
      }
    });
    if (!notified) console.warn('Error HTTP no manejado');
  }

  _notifyException(mensaje) {
    console.error('Excepción de aplicación:', mensaje);
    let notified = false;
    this.contexts.forEach(context => {
      if (context.onException && typeof context.onException === 'function') {
        try { context.onException(mensaje); notified = true; } catch (e) { console.error('Error en handler de excepciones:', e); }
      }
    });
    if (!notified) { console.warn('Excepción no manejada:', mensaje); if (typeof alert !== 'undefined') alert(mensaje); }
  }
}

const dispatcher = new CommandDispatcher();
export default dispatcher;
export { dispatcher as CommandDispatcher };
if (typeof window !== 'undefined') window.CommandDispatcher = dispatcher;
if (typeof globalThis !== 'undefined') globalThis.CommandDispatcher = dispatcher;
