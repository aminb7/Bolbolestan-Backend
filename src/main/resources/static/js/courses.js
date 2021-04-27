class SelectedCourses extends React.Component {
    constructor(props) {
        super(props);
        var initialStudent = {selectedCourses: {}}
        this.state = {student: initialStudent, isLoggedin: false};
    }

    componentWillMount() {
        fetch('loggedin_student')
            .then(resp => resp.json())
            .then(data => {
                this.setState(prevState => ({student: data, isLoggedin: true}));
            });
    }

    render() {
        if (!this.state.isLoggedin)
            return (<h1>Please Login First!</h1>);

        return (
            <div className="row">
                <section className="workbook_section">
                    <fieldset className="workbook">
                        <legend className="legend_text">دروس انتخاب شده</legend>
                        <div className="table-wrapper-scroll-y my-custom-scrollbar">
                            <table className="table table1 table_text">
                                <thead>
                                <tr className="blue_text">
                                    <th></th>
                                    <th>وضعیت</th>
                                    <th>کد</th>
                                    <th>نام درس</th>
                                    <th>استاد</th>
                                    <th>واحد</th>
                                </tr>
                                </thead>
                                <tbody className="gray_text">
                                <tr>
                                    <td><img src="../images/icons/012-trash-bin.png" className="trash_logo" alt="trash"/></td>
                                    <td><span className="green_bordered_text">ثبت شده</span></td>
                                    <td>۸۱۰۱۸۶۴-۰۱</td>
                                    <td>پایگاه داده ها</td>
                                    <td>آزاده شاکری</td>
                                    <th className="blue_text">۳</th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="line"></div>
                        <div className="row">
                            <p className="col-md-9 table_footer blue_text"><b>تعداد واحد ثبت شده: ۱۵</b></p>
                            <input type="image" src="../images/icons/009-refresh-arrow.png" className=" col-md-0 reload_icon"/>
                            <button type="submit" className="col-md-2.5 final_submit blue_text">ثبت نهایی</button>
                        </div>
                    </fieldset>
                </section>
            </div>
        );
    }
}

class SearchBar extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="row">
            </div>
        );
    }
}

class AllCourses extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="row">
            </div>
        );
    }
}

class CoursesPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <BolbolestanHeader src="courses" text1="خانه" text2="برنامه هفتگی" />
                <SelectedCourses />
                <SearchBar />
                <AllCourses />
                <BolbolestanFooter />
            </div>
        );
    }
}