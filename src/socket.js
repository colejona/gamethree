import io from 'socket.io-client';

export class Socket {
    constructor() {
        this.io = new io(this.getSocketAddress());
    }

    on(eventName, handler) {
        this.io.on(eventName, handler);
    }

    emit(eventName, data) {
        this.io.emit(eventName, data);
    }

    connected() {
        return this.io.connected;
    }

    getSocketAddress() {
        let address = window.location.protocol === 'https:' ? 'wss' : 'ws';
        address += `://${window.location.hostname}`;
        if (window.location.host.includes(":")) {
            address += `:${process.env.PORT || '5000'}`;
        }
        return address;
    }
}
