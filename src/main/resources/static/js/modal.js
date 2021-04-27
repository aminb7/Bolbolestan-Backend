class Modal extends React.Component {
    constructor(props) {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
    }

    handleLogout(event) {
        event.preventDefault();
        fetch('logout');
        ReactDOM.render(<LoginPage />, document.getElementById('app'));
    }

    handleCancel(event) {
        ReactDOM.render(<MainPage src={this.props.src} />, document.getElementById('app'));
    }

    render() {
        return <div>
            <div className="modal window" tabIndex="-1" role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">آیا می خواهید از حساب کاربری خود خارج شوید؟</h5>
                        </div>
                        <div className="modal-footer">
                            <button type="button" onClick={this.handleCancel} className="btn btn-primary btn-light cancel_btn modal_btn_size">انصراف
                            </button>
                            <button type="button" onClick={this.handleLogout} className="btn btn-secondary btn-success modal_btn_size"
                                    data-dismiss="modal">خروج
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container dark_background">
                <div className="modal" tabIndex="-1" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Modal title</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <p>Modal body text goes here.</p>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-primary">Save changes</button>
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    }
}

class MainPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if (this.props.src == 'home')
            return <HomePage />;
        if (this.props.src == 'courses')
            return <CoursesPage />;
        if (this.props.src == 'plan')
            return <PlanPage />;
    }
}
class ModalHomePage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <Modal src={this.props.src}/>
                <MainPage src={this.props.src}/>
            </div>
        );
    }
}
