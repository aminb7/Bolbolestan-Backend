class CoursesPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BolbolestanHeader />
                <SelectedCourses />
                <SearchBar />
                <AllCourses />
                <BolbolestanFooter />
            </div>
        );
    }
}