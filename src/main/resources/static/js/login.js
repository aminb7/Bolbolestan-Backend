class LoginPart extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container login_text">
                <h1>صفحه‌ی ورود</h1>

                <form className="login_form">
                    <div className="login_img_container">
                        <img src="../images/icons/login_avatar.png" alt="Avatar" className="login_avatar"/>
                    </div>

                    <div className="container login_container">
                        <label for="sid"><b>شماره دانشجویی</b></label>
                        <input type="text" placeholder="شماره دانشجویی خود را وارد کنید." className="login_input" name="sid" required/>
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
                <BolbolestanHeader />
                <LoginPart />
                <BolbolestanFooter />
            </div>
        );
    }
}