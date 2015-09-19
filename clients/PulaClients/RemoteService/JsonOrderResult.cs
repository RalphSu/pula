using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RemoteService
{
    public class JsonOrderResult<T>
    {
        public long pageSize { get; set; }

        public long startIndex { get; set; }

        public long totalRecords { get; set; }

        public long recordsReturned { get; set; }

        public IList<T> records { get; set; }

        public long totalPages { get; set; }
    }
}
