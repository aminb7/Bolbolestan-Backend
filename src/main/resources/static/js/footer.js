function ApplicationIcon(props) {
    return (
        <li className="nav-item">
            <a className="nav-link"><img className="footer_icon_size invert_image_color" src={props.src} alt={props.alt}></img></a>
        </li>
    );
}

function CopyRightIcon() {
    return (
        <img className="copy_right_icon_size invert_image_color" src="../images/icons/006-copyright.png" alt="copy right"></img>
    );
}

function FooterText() {
    return (
        <div className="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2">
            <div className="navbar-nav mr-auto order-0 nav-item active">
                <a className="nav-link footer_text">دانشگاه تهران - سامانه جامع بلبل‌ستان</a>
            </div>
        </div>
    );
}

function ApplicationIcons(props) {
    return (
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav ml-auto order-3">
                <ApplicationIcon src="../images/icons/003-twitter-logo-on-black-background.png" alt="twitter"/>
                <ApplicationIcon src="../images/icons/004-instagram.png" alt="instagram"/>
                <ApplicationIcon src="../images/icons/002-linkedin-logo.png" alt="linkedin"/>
                <ApplicationIcon src="../images/icons/005-facebook.png" alt="facebook"/>
            </ul>
        </div>
    );
}

function BolbolestanFooter() {
    return (
        <nav className="navbar navbar-expand-md sticky-top footer_color">
            <CopyRightIcon />
            <FooterText />
            <ApplicationIcons />
        </nav>
    );
}
