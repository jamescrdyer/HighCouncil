export class Message {
    public gameId: number;
    public fromUser: String;

    constructor(
        public toUsers: String[],
        public message: String
    ) {
    }
}
