function BolbolestanCarousel() {
    return <h1>BolbolestanCarousel</h1>;
}

class ProfileSection extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<h1>Profile</h1>);
    }
}

class TermGradesTable extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<h1>TermGradesTable</h1>);
    }
}

class GradesSection extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<h1>GradesSection</h1>);
    }
}

class HomePage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <BolbolestanHeader />
                <BolbolestanCarousel />
                <GradesSection />
                <ProfileSection />
                <BolbolestanFooter />
            </div>
        );
    }
}
