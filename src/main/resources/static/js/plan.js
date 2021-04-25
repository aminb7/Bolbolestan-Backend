class WeeklySchedule extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<h1>WeeklySchedule</h1>);
    }
}

class PlanPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BolbolestanHeader />
                <WeeklySchedule />
                <BolbolestanFooter />
            </div>
        );
    }
}