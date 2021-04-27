class LoginPart extends React.Component {
    constructor(props) {
        super(props);
        this.sendLogin = this.sendLogin.bind(this);
        this.handleSid = this.handleSid.bind(this);
        this.state = {sid : ''};
    }

    sendLogin(event) {
        event.preventDefault();
        var params = {
            "studentId": this.state.sid
        };
        var queryString = Object.keys(params).map(function(key) {
            return key + '=' + params[key]
        }).join('&');

        fetch('login?' + queryString)
            .then(response => response.json())
            .then((data) => {
                if (data == true) ReactDOM.render(<HomePage />, document.getElementById('app'));
                else alert('Student Id does not exists.');
            });
    }

    handleSid(event) {
        this.setState(prevState => ({sid: event.target.value}));
    }

    render() {
        return (
            <div className="container login_text">
                <h1>صفحه‌ی ورود</h1>

                <form onSubmit={this.sendLogin} className="login_form">
                    <div className="login_img_container">
                        <img src="../images/icons/login_avatar.png" alt="Avatar" className="login_avatar"/>
                    </div>

                    <div className="container login_container">
                        <label for="sid"><b>شماره دانشجویی</b></label>
                        <input type="text" placeholder="شماره دانشجویی خود را وارد کنید." className="login_input" onChange={this.handleSid} required/>
                        <button type="submit" className="login_button">ورود</button>
                    </div>
                </form>
            </div>
        );
    }
}

class LoginPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <LoginPart />
                <BolbolestanFooter />
            </div>
        );
    }
}