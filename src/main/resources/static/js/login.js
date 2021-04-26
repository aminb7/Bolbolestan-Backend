class LoginPart extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="container">
                <h1>Login Form</h1>

                <form class="login_form">
                    <div class="login_img_container">
                        <img src="../images/icons/login_avatar.png" alt="Avatar" class="login_avatar"/>
                    </div>

                    <div class="container login_container">
                        <label for="sid"><b>Student Id</b></label>
                        <input type="text" placeholder="Enter Student Id" class="login_input" name="sid" required/>
                        <button type="submit" class="login_button">Login</button>
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