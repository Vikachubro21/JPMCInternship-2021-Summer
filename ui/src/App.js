import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <div class="toolbar" role="banner">
        <img width="40" height=" 30" alt="JPMC Logo PLaceholder"
          src="https://th.bing.com/th/id/OIP.42US39t41lKv7d-hqR7ImQHaE8?pid=ImgDet&rs=1"></img>

        <h2>Cassandra Clusters Application</h2>

        <div class="spacer"></div>

      </div>

      <div class="row">
        <div class="column1">
          <div class="content" role="main">

            <h2>Table Information:</h2>
            <table border="1" id="table-stats">
              <tr>
                <th>Table Size</th>
                <th>Partition Sizes</th>
                <th>Number of Rows</th>
                <th>Number of Columns</th>
                <th>Number of Partitions</th>
                <th>Rows per Partition</th>
                <th>Column Definitions</th>
                <th>Column Types</th>
              </tr>
              <tr>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
              </tr>
              <tr>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
              </tr>
              <tr>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
              </tr>
              <tr>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
              </tr>

              <tr>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
                <td>sdf</td>
              </tr>
            </table>
          </div>
        </div>
        <div class="column2">

        </div>
        <div class="column3">
          <div class="sidebar-menu">
            <div class="logo">
              <br></br>
              <br></br>
              <br></br>
              <br></br>
              <h2>Cluster Information</h2>
            </div>
            <section class="container">
              <div class="one">
                <ul>
                  <li>
                    <p>Cluster Name:</p>
                  </li>
                  <li>
                    <p>Cluster Size:</p>
                  </li>
                  <li>
                    <p>Number of Keyspaces:</p>
                  </li>
                  <li>
                    <p>Number of Tables:</p>
                  </li>
                  <li>
                    <p>Number of Rows:</p>
                  </li>
                </ul>
              </div>
              <div class="two">
                <ul>
                  <li>
                    <p>D7329D209426CC3CAF298FC6CBC2B52B6A707BFC</p>
                  </li>
                  <li>
                    <p>D7329D209426CC3CAF298FC6CBC2B52B6A707BFC</p>
                  </li>
                  <li>
                    <p>D7329D209426CC3CAF298FC6CBC2B52B6A707BFC</p>
                  </li>
                  <li>
                    <p>D7329D209426CC3CAF298FC6CBC2B52B6A707BFC</p>
                  </li>
                  <li>
                    <p>D7329D209426CC3CAF298FC6CBC2B52B6A707BFC</p>
                  </li>
                </ul>
              </div>
            </section>
          </div>
        </div>

      </div>
    </div>
  );
}

export default App;
